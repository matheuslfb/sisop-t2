import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;

public class app {

	private static final String PATH = "src\\entrada2.txt";
	
	private static String mode;
	private static GerenteDeMemoria gm;

	public static void main(String[] args) throws IOException {

		String s =  readFileFromTerminal(PATH);
		File f = new File(s);

		FileReader fr;
		BufferedReader br;

		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			if (br.ready()) {
				mode = br.readLine().toLowerCase();

				String algo = br.readLine().toLowerCase();

				int pageSize = Integer.parseInt(br.readLine());
				int memorySize = Integer.parseInt(br.readLine());
				int diskSize = Integer.parseInt(br.readLine());

				gm = new GerenteDeMemoria(algo, pageSize, memorySize, diskSize);

				System.out.println("Modo: " + mode);
				System.out.println("Algoritmo de troca: " + algo);
				System.out.println("Tamanho da pagina: " + pageSize);
				System.out.println("Tamanho da memória: " + memorySize);
				System.out.println("tamanho do disco: " + diskSize);

				gm.criar(br);

			}

			br.close();
			fr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String readFileFromTerminal(String a) {
		Scanner s = new Scanner(a);
		return s.nextLine();
	}	

}
