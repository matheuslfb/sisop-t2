import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class GerenteDeMemoria {

	Scanner in = new Scanner(System.in);

	
	Algoritmo swap;
	int tamPaginas;
	int blocosMemoria;
	int blocosDisco;

	Memoria ram;
	Memoria disk;

	int qtdPaginas;

	/**
	 * Metodo construtor. Faz uso das primeiras informacoes disponibilizadas no
	 * arquivo de entrada.
	 */
	public GerenteDeMemoria(String algoritmo, int tamPaginas, int blocosRAM, int blocosSWAP) {
		// this.modo = modo; apenas sequencial
		this.tamPaginas = tamPaginas;
		this.blocosMemoria = blocosRAM;
		this.blocosDisco = blocosSWAP;
		qtdPaginas = blocosRAM / tamPaginas;
		swap = new Algoritmo(algoritmo.charAt(0), qtdPaginas);
	}

	/**
	 * Recebe cada linha do arquivo de entrada e chama as funcoes
	 * necessarias para atender esses comandos.
	 */
	public void run(String tipo, int processo, int enderecos) {
		System.out.println("\n\nComando: " + tipo + " P" + processo + " " + enderecos);

		if (tipo.equals("A"))
			acessaProcesso(processo, enderecos);
		else if (tipo.equals("M"))
			alocaMemoria(processo, enderecos);
		else if (tipo.equals("C"))
			criaProcesso(processo, enderecos);
		else if (tipo.equals("T")) {
			clean(processo);
		}
		ram.print();
		System.out.println();
		disk.print();
		System.out.println("\n\nPrecione qualquer tecla para continuar. ");
		in.next();
	}

	/**
	 * Monta a memoria virtual, com um ArrayList de paginas necessarias
	 */
	public void criaMemoriVirtual() {
		ArrayList<Pagina> paginas = new ArrayList<>();
		int numBloco = 0;
		System.out.println("\nEndereco de memoria RAM: ");
		for (int p = 0; p < tamPaginas; p++) {
			Pagina atual = new Pagina(p);
			System.out.print("\n" + atual.indice + " -\t");
			for (int b = 0; b < tamPaginas; b++) {
				Endereco e = new Endereco(numBloco);
				atual.enderecos.add(e);
				System.out.print(atual.enderecos.get(atual.enderecos.size() - 1).num + "\t");
				numBloco++;
			}
			paginas.add(atual);
		}
		ram = new Memoria(paginas);
	}

	
	/**
	 * Monta o disco, com um ArrayList de paginas necessarias
	 */
	public void criaDisco() {
		ArrayList<Pagina> paginas = new ArrayList<>();
		int numBloco = 0;
		System.out.println("\n\n Enderecos do disco - SWAP:");
		for (int p = 0; p < blocosDisco / tamPaginas; p++) {
			Pagina atual = new Pagina(p);
			System.out.print("\n" + atual.indice + " -\t");
			for (int b = 0; b < tamPaginas; b++) {
				Endereco e = new Endereco(numBloco);
				atual.enderecos.add(e);
				System.out.print(atual.enderecos.get(atual.enderecos.size() - 1).num + "\t");
				numBloco++;
			}
			paginas.add(atual);
		}
		disk = new Memoria(paginas);
	}

	/**
	 *  A partir do comando 'C' lido, verifica quantas páginas são necessárias
	 *  para criar um processo. A verificação é feita pelo método buscaPaginaVazia(int pg)
	 */
	private boolean criaProcesso(int procId, int solicitacao) {

		Processo p = new Processo(procId);
		int qntPaginas = solicitacao / tamPaginas;

		if (solicitacao % tamPaginas != 0) {
			qntPaginas++;
		}

		System.out.println("Quantidade de paginas necessarias para criar o processo: " + qntPaginas + " páginas");
		
		int[] paginasVazias = this.buscaPaginaVazia(qntPaginas);

		if (paginasVazias[0] == -1) {
			System.out.println("Nao ha espaco em RAM para criar o processo.");
			return false;
		}

		for (int i = 0; i < paginasVazias.length; i++) {
			for (Endereco endereco : ram.paginas.get(paginasVazias[i]).enderecos) {
				endereco.id = p.id;
				endereco.bloco = p.nextBloco;
				p.nextBloco++;
				solicitacao--;
				if (solicitacao == 0)
					return true;
			}
		}
		
		return true;
	}

	/**
	 * A partir do comando 'A', busca em disco e em memória o processo, retornando se o bloco foi
	 * encontrado ou nao.
	 */
	private boolean acessaProcesso(int procId, int target) {
		int contador = 0;
		for (Pagina p : ram.paginas) {
			for (Endereco e : p.enderecos) {
				if (e.id != null && e.id == procId) {
					contador++;
					if (e.id != null && e.bloco == target) {
						System.out.println("Processo acessado em memoria: endereco " + e.num);
						swap.atualizaLista(p.indice);
						return true;
					}
				}
			}
		}
		
		for (Pagina p : disk.paginas) {
			for (Endereco e : p.enderecos) {
				if (e.id != null && e.id == procId) {
					contador++;
					if (e.id != null && e.bloco == target) {
						System.out.println("Esta em disco");
						System.out.println("Processo acessado em disco: endereco " + e.num);
						trocaMemoriaDisco(p.indice);
						return true;
					}
				}
			}
		}
		System.out.println("Erro ao acessar processo." + " P" + procId + " " + contador + ":" + target);
		return false;
	}
	
	

	/**
	 * A partir do comando 'M', aloca mais espaco a um processo recursivamente
	 */
	private int alocaMemoria(int proc, int espacos) {

		int restante = preenche(proc, espacos);
		// se preencheu e tudo que dava na memoria e precisa mais
		if (restante > 0) {
			int indPaginaVazia = confereAlocacaoDisco();
			if (indPaginaVazia == -1) {
				System.out.println("Nao ha espaco disponivel em disco.");
			} else {
				System.out.println("Efetuando troca memoria-disco por PAGE FAULT.");
				trocaMemoriaDisco(indPaginaVazia);
				alocaMemoria(proc, restante);
			}
		}
		return 0;
	}
	
	
	/**
	 * Faz a troca de processos entre memoria e disco	 
	 */
	private void trocaMemoriaDisco(int pgDisco) {
		
		int menosRecente = swap.pagUsada();

		Pagina aux = new Pagina(-1);
		for (int b = 0; b < tamPaginas; b++) {
			Endereco e = new Endereco(b);
			aux.enderecos.add(e);
		}

		
		int cont = 0;
		for (Endereco end : aux.enderecos) {
			end.id = ram.paginas.get(menosRecente).enderecos.get(cont).id;
			end.bloco = ram.paginas.get(menosRecente).enderecos.get(cont).bloco;
			cont++;
		}
		
		for (Pagina memory : ram.paginas) {
			
			
			if (memory.indice == menosRecente) {
				int contEnd = 0;
				for (Endereco e : memory.enderecos) {
					e.id = disk.paginas.get(pgDisco).enderecos.get(contEnd).id;
					e.bloco = disk.paginas.get(pgDisco).enderecos.get(contEnd).bloco;
					contEnd++;
				}
				break;
			}
		}		
		for (Pagina disk : disk.paginas) {
			if (disk.indice == pgDisco) {
				int contEnd = 0;
				for (Endereco e : disk.enderecos) {
					e.id = aux.enderecos.get(contEnd).id;
					e.bloco = aux.enderecos.get(contEnd).bloco;
					contEnd++;
				}
			}
		}
	}

	/**
	 * Limpa a memória de disco e de memoria virtual.
	 */
	private void clean(int processo) {
		ram.limpar(processo);
		disk.limpar(processo);
	}

	/**
	 * A partir da leitura do comando 'C': busca as paginas livre para criar o processo.
	 */
	private int[] buscaPaginaVazia(int paginasNecessarias) {

		int[] paginasEncontradas = new int[paginasNecessarias];
		int cont = 0;

		for (int p = 0; p < qtdPaginas; p++) {
			if (ram.paginas.get(p).enderecos.get(0).id == null) {
				paginasEncontradas[cont] = p;
				cont++;
				swap.atualizaLista(p);
				if (cont == paginasNecessarias) {
					break;
				}
			}
		}

		// A "bomba" eh utilizada no create para indicar falta de Memoria
		if (cont < paginasNecessarias) {
			int[] bomb = new int[1];
			bomb[0] = -1;
			return bomb;
		}

		return paginasEncontradas;
	}

	/**
	 * A partir do comando 'M': aloca espacos para um processo em paginas onde ja
	 * retornando o numero de espaço alocado e se não foi possivel alocas
	 */
	private int preenche(int idProcesso, int qntEspaco) {
		int cont = qntEspaco;

		// primeiro preenche onde ja tem
		for (Pagina p : ram.paginas) {
			if (p.enderecos.get(0).id != null && p.enderecos.get(0).id == idProcesso) {
				for (Endereco e : p.enderecos) {
					if (e.id == null) {
						e.id = idProcesso;
						cont--;
						swap.atualizaLista(p.indice);
						if (cont == 0) {
							System.out.println("Espaço alocado com sucesso.");
							return 0;
						}
					}
				}
			}
		}
		for (Pagina p : ram.paginas) {
			if (p.enderecos.get(0).id == null) {
				for (Endereco e : p.enderecos) {
					if (e.id == null) {
						e.id = idProcesso;
						cont--;
						swap.atualizaLista(p.indice);
						if (cont == 0) {
							System.out.println("Espaço alocado com sucesso.");
							return 0;
						}
					}
				}
			}
		}
		System.out.println("Nao foi possivel alocar o espaco, restando " + cont + ".");
		return cont;
	}

	/**
	 * Faz uma busca por pagina vazia disponivel em disco, retornando o indice da página
	 * Retorna -1 se não ha nenhuma pagina.
	 */
	private int confereAlocacaoDisco() {
		for (Pagina d : disk.paginas) {
			if (d.enderecos.get(0).id == null) {
				// pagina livre
				return d.indice;
			}
		}
		return -1;
	}

	
}