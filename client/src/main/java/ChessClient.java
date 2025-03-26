import com.google.gson.Gson;
import com.google.gson.JsonObject;
import ui.EscapeSequences;

import java.util.Scanner;

public class ChessClient {
    private final Scanner scanner = new Scanner(System.in);
    private String authToken = "";
    ServerFacade serverFacade = new ServerFacade();
    private final Gson SERIALIZER = new Gson();

    public void run() throws Exception {
        System.out.print("♕ Welcome to 240 chess. Type 'help' to get started ♕");
        boolean loggedOut = true;
        boolean running = true;
        while (running) {
            if (loggedOut) {
                System.out.print("\n[LOGGED OUT] >>>");
            } else {
                System.out.print("\n[LOGGED IN] >>>");
            }
            String input = scanner.nextLine().trim();
            if (input.equals("help")) {
                printHelp(loggedOut);
            }
            if (input.equals("quit")) {
                System.out.print( EscapeSequences.SET_TEXT_COLOR_YELLOW + "See you soon!");
                running = false;
            }
            if (input.startsWith("register")) {
                String[] parts = input.split(" ");
                if (parts.length == 4) {
                    String username = parts[1];
                    String password = parts[2];
                    String email = parts[3];
                    String registerResponse = serverFacade.register(username, password, email);
                    JsonObject jsonResponse = SERIALIZER.fromJson(registerResponse, JsonObject.class);
                    if (registerResponse.contains("Error")) {
                        System.out.print(EscapeSequences.SET_BG_COLOR_RED +
                                "Registration failed. Please try again." +
                                EscapeSequences.RESET_TEXT_COLOR
                        );
                    } else {
                        authToken = jsonResponse.get("authToken").getAsString();
                        System.out.print("Logged in as " + username);
                        loggedOut = false;
                    }
                }
            }
            if (input.startsWith("login")) {
                String[] parts = input.split(" ");
                if (parts.length == 3) {
                    String username = parts[1];
                    String password = parts[2];
                    String loginResponse = serverFacade.login(username, password);
                    JsonObject jsonResponse = SERIALIZER.fromJson(loginResponse, JsonObject.class);
                    if (loginResponse.contains("Error")) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED +
                                "Error logging in." +
                                EscapeSequences.RESET_TEXT_COLOR
                        );
                    } else {
                        authToken = jsonResponse.get("authToken").getAsString();
                        System.out.print("Logged in as " + username);
                        loggedOut = false;
                    }
                }
            }
            if (input.equals("list") && !loggedOut) {
                String games = serverFacade.listGames(authToken);
                System.out.print(games);
            }
            if (input.startsWith("create") && !loggedOut) {
                String[] parts = input.split(" ");
                if (parts.length == 2) {
                    String gameName = parts[1];
                    String createGameResponse = serverFacade.createGame(gameName, authToken);
                    if (createGameResponse.contains("Error")) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED +
                                "Error creating game." +
                                EscapeSequences.RESET_TEXT_COLOR
                        );
                    } else {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN +
                                "Created game with name: " +
                                EscapeSequences.RESET_TEXT_COLOR +
                                gameName
                        );
                    }
                }
            }
        }
    }

    public void printHelp(boolean loggedOut) {
        String aGameText = EscapeSequences.RESET_TEXT_COLOR + " - a game\n";
        String quitString = EscapeSequences.SET_TEXT_COLOR_BLUE + "quit" + EscapeSequences.RESET_TEXT_COLOR + " - playing chess\n";
        String helpString = EscapeSequences.SET_TEXT_COLOR_BLUE + "help" + EscapeSequences.RESET_TEXT_COLOR + " - with possible commands\n";
        if (loggedOut) {
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
            System.out.print(quitString);
            System.out.print(helpString);
        } else {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                    "create <NAME>" +
                    aGameText
            );
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                    "list" +
                    EscapeSequences.RESET_TEXT_COLOR +
                    " - games\n"
            );
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                    "join <ID> [WHITE|BLACK]" +
                    aGameText
            );
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                    "observe <ID>" +
                    aGameText
            );
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                    "logout" +
                    EscapeSequences.RESET_TEXT_COLOR +
                    " - when you are done\n"
            );
            System.out.print(quitString);
            System.out.print(helpString);
        }
    }
}
