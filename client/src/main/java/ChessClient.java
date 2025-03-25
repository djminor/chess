import ui.EscapeSequences;

import java.util.Scanner;

public class ChessClient {
    private final Scanner scanner = new Scanner(System.in);
    ServerFacade serverFacade = new ServerFacade();

    public void run() throws Exception {
        System.out.print("♕ Welcome to 240 chess. Type 'help' to get started ♕");
        boolean running = true;
        while (running) {
            System.out.print("\n[LOGGED OUT] >>>");
            String input = scanner.nextLine().trim();
            if (input.equals("help")) {
                printHelp();
            }
            if (input.equals("quit")) {
                System.out.print( EscapeSequences.SET_TEXT_COLOR_YELLOW + "See you soon!");
                running = false;
            }
            if (input.startsWith("register ")) {
                String[] parts = input.split(" ");
                if (parts.length == 4) {
                    String username = parts[1];
                    String password = parts[2];
                    String email = parts[3];
                    String registerResponse = serverFacade.register(username, password, email);
                    if (registerResponse.contains("Error")) {
                        System.out.print("Registration failed. Please try again.");
                    } else {
                        System.out.print("Logged in as " + username);
                    }
                }
            }
        }
    }

    public void printHelp() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                "register <USERNAME> <PASSWORD> <EMAIL>" +
                EscapeSequences.RESET_TEXT_COLOR +
                " - to create an account\n"
        );
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                "login <USERNAME> <PASSWORD>" +
                EscapeSequences.RESET_TEXT_COLOR +
                " - to play chess\n"
        );
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                "quit" +
                EscapeSequences.RESET_TEXT_COLOR +
                " - playing chess\n"
        );
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                "help" +
                EscapeSequences.RESET_TEXT_COLOR +
                " - with possible commands"
        );
    }
}
