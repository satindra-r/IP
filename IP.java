import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class IP {
	long[] emptyline = new long[] { 0, 0, 0, 0, 0 };
	int[] memory = new int[1000];
	String[] code;
	long[][] tokens;
	HashMap<Long, Integer> tags = new HashMap<Long, Integer>();
	Stack<Integer> callstack = new Stack<Integer>();
	int lineNo = 0;

	public boolean runline() {
		// TODO
		try {
			System.out.println("token:" + tokens[lineNo][0] + "-" + tokens[lineNo][1] + "-" + tokens[lineNo][2] + "-"
					+ tokens[lineNo][3] + "-" + tokens[lineNo][4]);
			lineNo++;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void validateLine(int validateLineNo) {
		String codeLine = code[validateLineNo];
		try {
			if (!codeLine.startsWith("//")) {
				String[] ip = codeLine.split(":");
				for (int i = 0; i < ip.length; i++) {
					while (ip[i].length() < 4) {
						ip[i] = "0" + ip[i];
					}
				}
				tokens[validateLineNo][0] = Integer.parseInt(ip[0] + ip[1], 16);
				tokens[validateLineNo][1] = Integer.parseInt(ip[2] + ip[3], 16);
				tokens[validateLineNo][2] = Integer.parseInt(ip[4], 16);
				tokens[validateLineNo][3] = Integer.parseInt(ip[5] + ip[6], 16);
				tokens[validateLineNo][4] = Integer.parseInt(ip[7], 16);

				if (!(tokens[validateLineNo][0] >= 0 && tokens[validateLineNo][0] <= 4294967295L
						&& tokens[validateLineNo][1] >= 0 && tokens[validateLineNo][1] <= 4294967295L
						&& tokens[validateLineNo][2] >= 0 && tokens[validateLineNo][2] <= 65535
						&& tokens[validateLineNo][3] >= 0 && tokens[validateLineNo][3] <= 4294967295L
						&& tokens[validateLineNo][4] >= 0 && tokens[validateLineNo][4] <= 65535)) {
					tokens[validateLineNo] = emptyline;
				} else if (tokens[validateLineNo][4] == 65535) {
					tags.put(tokens[validateLineNo][0], validateLineNo);
					tokens[validateLineNo] = emptyline;
				}
			} else {
				tokens[validateLineNo] = emptyline;
			}
		} catch (Exception e) {
			tokens[validateLineNo] = emptyline;
		}
	}

	public void init(String filename) {
		try {
			for (int i = 0; i < memory.length; i++) {
				memory[i] = 0;
			}
			ArrayList<String> codeLoader = new ArrayList<String>();
			Scanner sc;
			sc = new Scanner(new File(filename));
			while (sc.hasNextLine()) {
				codeLoader.add(sc.nextLine().strip());
			}
			code = new String[codeLoader.size()];
			if (true) {
				int i = 0;
				for (Iterator<String> iterator = codeLoader.iterator(); iterator.hasNext();) {
					code[i] = iterator.next();
					i++;
				}
			}
			tokens = new long[code.length][5];
			for (int i = 0; i < code.length; i++) {
				validateLine(i);
			}
			while (runline())
				;
			sc.close();
		} catch (FileNotFoundException e) {
			System.err.print("File not found");
		}

	}

	public static void main(String[] args) {
		IP ip = new IP();
		ip.init(args[0]);
	}
}
