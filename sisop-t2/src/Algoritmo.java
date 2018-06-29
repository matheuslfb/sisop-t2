import java.util.LinkedList;
import java.util.Random;

public class Algoritmo {
	boolean lru;
	LinkedList<Integer> qntPagUsadas = new LinkedList<>();
	Random aleatoria = new Random();
	int qtdPaginas;

	public Algoritmo(char inicial, int qtdPaginas) {
		if (inicial == 'l') {
			lru = true;
		}
		this.qtdPaginas = qtdPaginas;
	}

	public void atualizaLista(int pagina) {
		if (qntPagUsadas.contains(pagina)) {
			qntPagUsadas.add(qntPagUsadas.remove(qntPagUsadas.lastIndexOf(pagina)));
		} else {
			qntPagUsadas.add(pagina);
		}
	}

	public int pagUsada() {
		if (lru) {
			return qntPagUsadas.removeFirst();
		} else {
			
			return aleatoria.nextInt(qtdPaginas);
		}
	}

	
}
