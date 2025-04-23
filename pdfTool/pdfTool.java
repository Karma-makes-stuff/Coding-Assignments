import java.io.*; //Exception
import java.util.Scanner; //Eingabe
import org.apache.pdfbox.pdmodel.*; 
import org.apache.pdfbox.text.*; 

public class pdfTool{

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		boolean done = false;
		while (!done){
			String mode = "0";

			//select mode
			while (!mode.equals("suchen") && !mode.equals("ersetzen")){
				System.out.println("Gib einen Modus an: suchen oder ersetzen");
				mode = scan.next();
			}

			System.out.println("Gib einen Dateinamen an.");
			String fName = scan.next();
			System.out.println("Gib einen Suchbegriff ein.");
			String sWord = scan.next();
			if (mode.equals("ersetzen")){
				System.out.println("Gib das Wort zum ersetzen an.");
				String rWord = scan.next();
				try {
					replaceWord(fName, sWord, rWord);
				} catch (IOException e) {
					System.out.println("Text konnte nicht extrahiert werden.");
					File tempFile = new File(fName);
					if (!tempFile.exists())System.out.println("Datei konnte nicht gefunden werden.");
					System.out.println("Stacktrace drucken? j/N");
					if (scan.next().equals("j"))e.printStackTrace();
				}
				finally{
					System.out.println("Weitere Daten verarbeiten? J/n");
					if (scan.next().equals("n")) done = true;
					else done = false;
				}
				
			}
			else{
				try {
					seekWord(fName, sWord);
				} catch (Exception e) {
					System.out.println("Text konnte nicht extrahiert werden.");
					File tempFile = new File(fName);
					if (!tempFile.exists())System.out.println("Datei konnte nicht gefunden werden.");
					System.out.println("Stacktrace drucken? j/N");
					if (scan.next().equals("j"))e.printStackTrace();
				}
				finally{
					System.out.println("Weitere Daten verarbeiten? J/n");
					if (scan.next().equals("n")) done = true;
					else done = false;
				}
	
			}
		}
		scan.close();
		
		/* old debugging stuff
		try {
		    String foo = readPDF("dokument.pdf");
		    System.out.println(foo);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		try {
		    seekWord("dokument.pdf", "ist");
		} catch (IOException e) {
		    e.printStackTrace();
		}

		try {
		    replaceWord("dokument.pdf", "ist", "Popo");
		} catch (IOException e) {
		    e.printStackTrace();
		}
		*/
	}	



	public static String readPDF(String fName) throws IOException {
		String outStr = fName;

		File rfile = new File(fName);
		PDDocument rdoc = PDDocument.load(rfile);

		PDFTextStripper pdfStripper = new PDFTextStripper();
		outStr = pdfStripper.getText(rdoc);
		rdoc.close();
	
		return outStr;
	}


	public static void seekWord(String fName, String sWord) throws IOException{

		String text = readPDF(fName);

		String regex="[ ]"; //separator
		String[] words = text.split(regex); //makes array of Strings
		int counter = 0; // number of occurances 
		int position = 0; //pointer value for wordFound
		boolean[] wordFound = new boolean[words.length]; //Is the word here?
		for (String word:words){ //if the word matches, note that in wordFound
			if (sWord.equals(word)){
				counter++;
				wordFound[position] = true;
			}
			position++;
		}
		if (counter == 0 ) System.out.println("Wort nicht gefunden :(");
		else{
			System.out.println("\"" + sWord + "\" " + counter + " mal gefunden an den Stellen:");
			int position2 = 0;
			for (boolean found:wordFound){ //iterates through wordFound using position2 as a pointer
				position2++;
				if (found) System.out.print(position2 + " ");
			}
			System.out.println();
		}

	}


	public static void replaceWord(String fName, String sWord, String rWord) throws IOException{

		String text = readPDF(fName);

		String regex="[ ]"; //separator
		String[] words = text.split(regex); //makes array of Strings
		for (String word:words){ //replaces matching words with rWord
			if (sWord.equals(word)){
				word = rWord;
			}
			System.out.print(word + " ");
		}
	}


}

