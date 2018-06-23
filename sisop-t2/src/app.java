import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.Scanner;

public class app {

	private static final String PATH = "src\\entrada1.txt";
	private static final boolean DEBUG = true;
	public static String mode;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String s = DEBUG ? PATH : readFileFromTerminal();
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
				
				System.out.println("Modo: " + mode);
				System.out.println("Algoritmo de troca: " + algo);
				System.out.println("Tamanho da pagina: "  + pageSize);
				System.out.println("tamanho do disco: " + diskSize);
				
				
			
			}
			
			br.close();
			fr.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String readFileFromTerminal() {
		Scanner s = new Scanner(System.in);
		return s.nextLine();
	}

	private static void inicia(BufferedReader br) throws IOException {
		mode = br.readLine().toLowerCase();

	}

	private static boolean programMode() {
		return mode.equals("sequencial") || mode.equals("s") || mode.equals("0");
	}

}
