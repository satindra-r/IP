import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

public class IP {
	long[] emptyline = new long[] { 0, 0, 0, 0, 0 };
	HashMap<Integer, Integer> memory = new HashMap<Integer, Integer>();
	String[] code;
	long[][] tokens;
	HashMap<Integer, Integer> tags = new HashMap<Integer, Integer>();
	Stack<Integer> callStack = new Stack<Integer>();
	int lineNo = 0;
	Scanner sc;
	String inputBuffer = "";
	int inputStatus = 0;

	public void memset(int index, int value) {
		if (value == 0) {
			if (memory.containsKey(index)) {
				memory.remove(index);
			}
		} else {
			if (memory.containsKey(index)) {
				memory.replace(index, value);
			} else {
				memory.put(index, value);
			}
		}

	}

	public int memget(int index) {
		return memory.getOrDefault(index, 0);
	}

	public int calculate1(int operand1, int operator1, int operand2) {
		// TODO
		int output = 0;
		switch (operator1 % 4096) {
		case 0:
			output = operand2;
			break;
		case 1:
			output = memget(operand2);
			break;
		case 2:
			output = Math.abs(operand2);
			break;
		case 3:
			output = operand1 + operand2;
			break;
		case 4:
			output = operand1 - operand2;
			break;
		case 5:
			output = operand2 - operand1;
			break;
		case 6:
			output = operand1 * operand2;
			break;
		case 7:
			output = operand1 / operand2;
			break;
		case 8:
			output = operand2 / operand1;
			break;
		case 9:
			output = operand1 % operand2;
			break;
		case 10:
			output = operand2 % operand1;
			break;
		case 11:
			output = (int) Math.pow(operand1, operand2);
			break;
		case 12:
			output = (int) Math.pow(operand2, operand1);
			break;
		case 13:
			output = operand1 & operand2;
			break;
		case 14:
			output = operand1 | operand2;
			break;
		case 15:
			output = ~operand2;
			break;

		}
		return output;
	}

	public int calculate2(int destination, int operator2, int output1) {
		// TODO
		int output = 0;
		output = calculate1(destination, operator2, output1);
		switch (operator2 / 4096) {
		case 0:
			lineNo++;
			break;
		case 1:
			System.out.print(output);
			lineNo++;
			break;
		case 2:
			if (output > 0) {
				System.out.print((char) output);
			}
			lineNo++;
			break;
		case 3:
			if (output > 0) {
				System.out.print((char) output);
			}
			output = sc.nextInt();
			sc.nextLine();
			lineNo++;
			break;
		case 4:
			if (output > 0) {
				System.out.print((char) output);
			}
			if (inputBuffer.length() == 0) {
				inputBuffer = sc.nextLine() + "\n";
			}
			output = inputBuffer.charAt(0);
			inputBuffer = inputBuffer.substring(1);
			lineNo++;
			break;
		case 5:
			if (output > 0) {
				lineNo = tags.get(destination);
			} else {
				lineNo++;
			}
			break;
		case 6:
			if (output > 0) {
				lineNo = tags.get(output);
			} else {
				lineNo++;
			}
			break;
		case 7:
			if (output > 0) {
				callStack.add(lineNo);
				lineNo = tags.get(destination);
			} else {
				lineNo++;
			}
			break;
		case 8:
			if (output > 0) {
				callStack.add(lineNo);
				lineNo = tags.get(output);
			} else {
				lineNo++;
			}
			break;
		case 9:
			lineNo = callStack.pop();
			break;
		case 10:
			lineNo++;
			break;
		case 14:
			sc.close();
			System.exit(0);
			break;
		}
		return output;

	}

	public boolean runline() {
		int operand1;
		int operand2;
		int operator1 = (int) tokens[lineNo][2];
		int operator2 = (int) tokens[lineNo][4];
		//System.out.println("Lineno" + lineNo);

		if (tokens[lineNo][1] > Integer.MAX_VALUE) {
			operand1 = memget((int) (tokens[lineNo][1] - Integer.MAX_VALUE));
		} else {
			operand1 = (int) (tokens[lineNo][1]);
		}

		if (tokens[lineNo][3] > Integer.MAX_VALUE) {
			operand2 = memget((int) (tokens[lineNo][3] - Integer.MAX_VALUE));
		} else {
			operand2 = (int) (tokens[lineNo][3]);
		}

		if (tokens[lineNo][0] > Integer.MAX_VALUE) {
			if (operator2 / 4096 == 10) {
				memset(memget((int) (tokens[lineNo][0] - Integer.MAX_VALUE)),
						calculate2(memget((int) (tokens[lineNo][0] - Integer.MAX_VALUE)), operator2,
								calculate1(operand1, operator1, operand2)));
				// System.out.println(memget(memget((int) (tokens[lineNo][0] -
				// Integer.MAX_VALUE))));
			} else {
				memset((int) (tokens[lineNo][0] - Integer.MAX_VALUE),
						calculate2(memget((int) (tokens[lineNo][0] - Integer.MAX_VALUE)), operator2,
								calculate1(operand1, operator1, operand2)));
			}

		} else {
			calculate2((int) (tokens[lineNo][0]), operator2, calculate1(operand1, operator1, operand2));
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
				tokens[validateLineNo][0] = Long.parseLong(ip[0] + ip[1], 16);
				tokens[validateLineNo][1] = Long.parseLong(ip[2] + ip[3], 16);
				tokens[validateLineNo][2] = Integer.parseInt(ip[4], 16);
				tokens[validateLineNo][3] = Long.parseLong(ip[5] + ip[6], 16);
				tokens[validateLineNo][4] = Integer.parseInt(ip[7], 16);

				if (!(tokens[validateLineNo][0] >= 0 && tokens[validateLineNo][0] <= 4294967295L
						&& tokens[validateLineNo][1] >= 0 && tokens[validateLineNo][1] <= 4294967295L
						&& tokens[validateLineNo][2] >= 0 && tokens[validateLineNo][2] <= 65535
						&& tokens[validateLineNo][3] >= 0 && tokens[validateLineNo][3] <= 4294967295L
						&& tokens[validateLineNo][4] >= 0 && tokens[validateLineNo][4] <= 65535)) {
				} else if (tokens[validateLineNo][4] == 65535) {
					tags.put((int) (tokens[validateLineNo][0]), validateLineNo);
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
			ArrayList<String> codeLoader = new ArrayList<String>();
			Scanner file;
			file = new Scanner(new File(filename));
			while (file.hasNextLine()) {
				codeLoader.add(file.nextLine().strip());
			}
			sc = new Scanner(System.in);

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
			while (true)
				runline();
		} catch (FileNotFoundException e) {
			System.err.print("File not found");
		}

	}

	public static void main(String[] args) {
		IP ip = new IP();
		ip.init(args[0]);
	}
}
/*
 * 8000:0000:0000:0000:0000:0000:0048:0000
 * 8000:0001:0000:0000:0000:0000:0065:0000
 * 8000:0002:0000:0000:0000:0000:006C:0000
 * 8000:0003:0000:0000:0000:0000:006C:0000
 * 8000:0004:0000:0000:0000:0000:006F:0000
 * 8000:0005:0000:0000:0000:0000:0020:0000
 * 8000:0006:0000:0000:0000:0000:0057:0000
 * 8000:0007:0000:0000:0000:0000:006F:0000
 * 8000:0008:0000:0000:0000:0000:0072:0000
 * 8000:0009:0000:0000:0000:0000:006C:0000
 * 8000:000A:0000:0000:0000:0000:0064:0000
 * 8000:000B:0000:0000:0000:0000:0021:0000
 * 
 * 8000:00F1:0000:0000:0000:0000:0001:0000
 * 8000:00F2:0000:0000:0000:0000:000D:0000
 * 1000:0000:0000:0000:0000:0000:0000:FFFF
 * 0000:0000:0000:0000:0001:8000:00F1:2000
 * 8000:00F1:0000:0000:0000:0000:0001:0003
 * 1000:0000:8000:00F2:0004:8000:00F1:5000
 * 0000:0000:0000:0000:0000:0000:0000:E000
 */
