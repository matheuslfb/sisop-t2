import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.*;
import java.util.Scanner;

public class app {

	private static String modo;
	private static String algoritmo;
	private static int tamPagina;
	private static int enderecoRam;
	private static int enderecoSWAP;

	public static void main(String[] args) throws IOException {

		readFileFromTerminal();
	}

	private static void readFileFromTerminal() {
		Scanner in = new Scanner(System.in);

		Path path = Paths.get(System.getProperty("user.dir") + "\\src\\entrada1.txt");
		char os = System.getProperty("os.name").charAt(0);
		if (os == 'L' || os == 'M') {
			path = Paths.get(System.getProperty("user.dir") + "//src//entrada1.txt");
		}

		try (BufferedReader br = Files.newBufferedReader(path, Charset.defaultCharset())) {
			Scanner sc = new Scanner(path);

			modo = sc.nextLine();
			algoritmo = sc.nextLine();
			tamPagina = sc.nextInt();
			enderecoRam = sc.nextInt();
			enderecoSWAP = sc.nextInt();

			GerenteDeMemoria gerente = new GerenteDeMemoria(algoritmo, tamPagina, enderecoRam, enderecoSWAP);

			gerente.criaMemoriVirtual();
			gerente.criaDisco();

			String tipo;
			String nome;
			int processo = 0;
			int enderecos = 0;

			// Faz a leitura de cada linha
			while (sc.hasNext()) {
				enderecos = 0;
				tipo = sc.next();
				if (!tipo.equals("T")) {
					nome = sc.next();
					processo = Integer.valueOf(nome.substring(1));
					enderecos = sc.nextInt();
				} else {
					nome = sc.next();
					processo = Integer.valueOf(nome.substring(1));
				}

				gerente.run(tipo, processo, enderecos);
			}
			System.out.print("\n-Final da execução-");
		} catch (IOException e) {
			System.err.format("Erro de I/O", e);
		}

	}

}
