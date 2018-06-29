import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class GerenteDeMemoria {

	Scanner in = new Scanner(System.in);

	private Algoritmo swap;
	private int tamPaginas;
	private int blocosMemoria;
	private int blocosDisco;

	private Memoria ram;
	private Memoria disk;

	private int qtdPaginas;

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
	 * Funcao principal.
	 * 
	 * Recebe cada linha do arquivo de entrada e chama as funcoes necessarias para
	 * atender esses comandos.
	 */
	public void run(String comando, int processo, int endMemoria) {
		System.out.println("\n\nComando: " + comando + " P" + processo + " " + endMemoria);

		if (comando.equals("A"))
			acessaProcesso(processo, endMemoria);
		else if (comando.equals("M"))
			alocaMemoria(processo, endMemoria);
		else if (comando.equals("C"))
			criaProcesso(processo, endMemoria);
		else if (comando.equals("T")) {
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
	 * A partir do comando 'C' lido, verifica quantas páginas são necessárias
	 * para criar um processo. A verificação é feita pelo método
	 * buscaPaginaVazia(int pg)
	 */
	private boolean criaProcesso(int procId, int tamProcesso) {

		Processo p = new Processo(procId);
		int qntPaginas = tamProcesso / tamPaginas;

		if (tamProcesso % tamPaginas != 0) {
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
				tamProcesso--;
				if (tamProcesso == 0)
					return true;
			}
		}

		return true;
	}

	/**
	 * A partir do comando 'A', busca em disco e em memória o processo, retornando
	 * se o bloco foi encontrado ou nao.
	 */
	private boolean acessaProcesso(int procId, int blocoDeMemoria) {
		int contador = 0;
		for (Pagina p : ram.paginas) {
			for (Endereco e : p.enderecos) {
				if (e.id != null && e.id == procId) {
					contador++;
					if (e.id != null && e.bloco == blocoDeMemoria) {
						System.out.println("Processo acessado em memoria: endereco " + e.num);
						swap.refreshPaginas(p.indice);
						return true;
					}
				}
			}
		}

		for (Pagina p : disk.paginas) {
			for (Endereco e : p.enderecos) {
				if (e.id != null && e.id == procId) {
					contador++;
					if (e.id != null && e.bloco == blocoDeMemoria) {
						System.out.println("Esta em disco");
						System.out.println("Processo acessado em disco: endereco " + e.num);
						trocaMemoriaDisco(p.indice);
						return true;
					}
				}
			}
		}
		System.out.println("Erro ao acessar processo." + " P" + procId + " " + contador + ":" + blocoDeMemoria);
		return false;
	}

	/**
	 * A partir do comando 'M', aloca mais espaco a um processo recursivamente
	 */
	private int alocaMemoria(int processo, int espacosDeMemoria) {

		int restante = preenche(processo, espacosDeMemoria);

		if (restante > 0) {
			int indPaginaVazia = confereAlocacaoDisco();
			if (indPaginaVazia == -1) {
				System.out.println("Nao ha espaco disponivel em disco.");
			} else {
				System.out.println("Efetuando troca memoria-disco por PAGE FAULT.");
				trocaMemoriaDisco(indPaginaVazia);
				alocaMemoria(processo, restante);
			}
		}
		return 0;
	}
	
	/**
	 * A partir do comando 'M': aloca espacos para um processo em paginas 
	 * retornando o numero de espaço alocado e se não foi possivel alocas
	 */
	private int preenche(int idProcesso, int qntEspaco) {
		int cont = qntEspaco;

		for (Pagina p : ram.paginas) {
			if (p.enderecos.get(0).id != null && p.enderecos.get(0).id == idProcesso) {
				for (Endereco e : p.enderecos) {
					if (e.id == null) {
						e.id = idProcesso;
						cont--;
						swap.refreshPaginas(p.indice);
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
						swap.refreshPaginas(p.indice);
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
				int contMemory = 0;
				for (Endereco e : memory.enderecos) {
					e.id = disk.paginas.get(pgDisco).enderecos.get(contMemory).id;
					e.bloco = disk.paginas.get(pgDisco).enderecos.get(contMemory).bloco;
					contMemory++;
				}
				break;
			}
		}
		for (Pagina disk : disk.paginas) {
			if (disk.indice == pgDisco) {
				int contDisk = 0;
				for (Endereco e : disk.enderecos) {
					e.id = aux.enderecos.get(contDisk).id;
					e.bloco = aux.enderecos.get(contDisk).bloco;
					contDisk++;
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
	 * A partir da leitura do comando 'C': busca as paginas livre para criar o
	 * processo. Retorna -1, caso n�o tenha paginas disponiveis.
	 */
	private int[] buscaPaginaVazia(int paginasNecessarias) {

		int[] paginasEncontradas = new int[paginasNecessarias];
		int cont = 0;

		for (int p = 0; p < qtdPaginas; p++) {
			if (ram.paginas.get(p).enderecos.get(0).id == null) {
				paginasEncontradas[cont] = p;
				cont++;
				swap.refreshPaginas(p);
				if (cont == paginasNecessarias) {
					break;
				}
			}
		}

		if (cont < paginasNecessarias) {
			int[] faltaPagina = new int[1];
			faltaPagina[0] = -1;
			return faltaPagina;
		}

		return paginasEncontradas;
	}

	

	/**
	 * Faz uma busca por pagina vazia disponivel em disco, retornando o indice da
	 * página Retorna -1 se não ha nenhuma pagina.
	 */
	private int confereAlocacaoDisco() {
		for (Pagina d : disk.paginas) {
			if (d.enderecos.get(0).id == null) {
				return d.indice;
			}
		}
		return -1;
	}

}