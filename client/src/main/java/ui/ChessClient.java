package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Objects;
import java.util.Scanner;

public class ChessClient {
    private final Scanner scanner = new Scanner(System.in);
    private String authToken = "";
    private String globalUsername = "";
    private static final int SERVER_PORT = 8080;
    ServerFacade serverFacade = new ServerFacade(SERVER_PORT);
    private static final Gson SERIALIZER = new Gson();
    private final ChessBoard board = new ChessBoard();

    public void run() throws Exception {
        System.out.print("♕ Welcome to 240 chess. Type 'help' to get started ♕");
        boolean loggedOut = true;
        boolean running = true;
        boolean validInput = false;
        while (running) {
            if (loggedOut) {
                System.out.print("\n[LOGGED OUT] >>>");
            } else {
                System.out.print("\n[LOGGED IN] >>>");
            }
            String input = scanner.nextLine().trim();
            if (input.equals("help")) {
                validInput = true;
                printHelp(loggedOut);
            }
            if (input.equals("quit")) {
                validInput = true;
                System.out.print( EscapeSequences.SET_TEXT_COLOR_YELLOW + "See you soon!");
                running = false;
            }
            if (input.startsWith("register")) {
                validInput = true;
                String[] parts = input.split(" ");
                int expectedLength = 4;
                if (parts.length == expectedLength) {
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
                } else {
                    badInputLength(expectedLength, parts.length);
                }
            }
            if (input.startsWith("login")) {
                validInput = true;
                String[] parts = input.split(" ");
                int expectedLength = 3;
                if (parts.length == expectedLength) {
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
                        globalUsername = username;
                        System.out.print("Logged in as " + username + "\n");
                        loggedOut = false;
                    }
                }
                else {
                    badInputLength(expectedLength, parts.length);
                }
            }
            if (input.equals("list") && !loggedOut) {
                validInput = true;
                String games = serverFacade.listGames(authToken);
                JsonObject reqJson = SERIALIZER.fromJson(games, JsonObject.class);
                JsonArray gamesArray = reqJson.getAsJsonArray("games");
                printGames(gamesArray);
            }
            if (input.startsWith("create") && !loggedOut) {
                validInput = true;
                String[] parts = input.split(" ");
                int expectedLength = 2;
                if (parts.length == expectedLength) {
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
                } else {
                    badInputLength(expectedLength, parts.length);
                }
            }
            if (input.startsWith("join") && !loggedOut) {
                validInput = true;
                String[] parts = input.split(" ");
                int expectedLength = 3;
                if (parts.length == expectedLength) {
                    int gameId = Integer.parseInt(parts[1]);
                    String userColor = parts[2];
                    String joinGameResponse = serverFacade.joinGame(userColor, gameId, authToken);
                    if (joinGameResponse.contains("Error")) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED +
                                "Error joining game." +
                                EscapeSequences.RESET_TEXT_COLOR
                        );
                    } else {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN +
                                "Joined game with id " +
                                EscapeSequences.RESET_TEXT_COLOR +
                                gameId +
                                EscapeSequences.SET_TEXT_COLOR_GREEN +
                                " and set " +
                                userColor +
                                " username to " +
                                EscapeSequences.RESET_TEXT_COLOR +
                                globalUsername +
                                "\n"
                        );
                        displayBoard(userColor);
                    }
                } else {
                    badInputLength(expectedLength, parts.length);
                }
            }
            if (input.equals("logout") && !loggedOut) {
                validInput = true;
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE +
                        "Logging out..." +
                        EscapeSequences.RESET_TEXT_COLOR
                );
                String logoutResponse = serverFacade.logout(authToken);
                if(logoutResponse.contains("Error")) {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_RED +
                            "Error logging out" +
                            EscapeSequences.RESET_TEXT_COLOR
                    );
                } else {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW +
                            "\nSuccess!" +
                            EscapeSequences.RESET_TEXT_COLOR
                    );
                    loggedOut = true;
                }
            }
            if (input.startsWith("observe") && !loggedOut) {
                validInput = true;
                String[] parts = input.split(" ");
                int expectedLength = 2;
                if (parts.length == expectedLength) {
                    displayBoard("WHITE");
                } else {
                    badInputLength(expectedLength, parts.length);
                }
            }
            else if (!input.isEmpty() && !validInput){
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED +
                        "Unrecognized input. Type 'help' to see list of commands" +
                        EscapeSequences.RESET_TEXT_COLOR
                );
            }
        }
    }

    private void printHelp(boolean loggedOut) {
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
    private void printGames(JsonArray games) {
        int listNumber = 1;
        for (JsonElement gameElement : games) {
            JsonObject game = gameElement.getAsJsonObject();
            String gameName = game.get("gameName").getAsString();
            String whiteUsername = game.get("whiteUsername").getAsString();
            String blackUsername = game.get("blackUsername").getAsString();
            System.out.print(listNumber + ". " + EscapeSequences.SET_TEXT_COLOR_BLUE + gameName + EscapeSequences.RESET_TEXT_COLOR);
            System.out.print("\n  |\n   -- White Username: " +
                    EscapeSequences.SET_TEXT_COLOR_BLUE +
                    whiteUsername +
                    EscapeSequences.RESET_TEXT_COLOR +
                    "\n"
            );
            System.out.print("  |\n   -- Black Username: " +
                    EscapeSequences.SET_TEXT_COLOR_BLUE +
                    blackUsername +
                    EscapeSequences.RESET_TEXT_COLOR +
                    "\n"
            );
            listNumber += 1;
        }
    }
    private void displayBoard(String playerColor) {
        board.resetBoard();

        System.out.println("    A  B  C  D  E  F  G  H  ");
        System.out.println("  +------------------------+");

        boolean isWhite = Objects.equals(playerColor, "WHITE");

        for (int row = (isWhite ? 1 : 8); (isWhite ? row <= 8 : row >= 1); row += (isWhite ? 1 : -1)) {
            System.out.print((9 - row) + " |");
            for (int col = (isWhite ? 1 : 8); (isWhite ? col <= 8 : col >= 1); col += (isWhite ? 1 : -1)) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                String bgColor = ((row + col) % 2 == 0) ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                String printCharacter = printSquare(piece);
                System.out.print(bgColor + printCharacter + EscapeSequences.RESET_BG_COLOR);
            }
            System.out.println("| " + (9 - row));
        }

        System.out.println("  +------------------------+");
        System.out.println("    A  B  C  D  E  F  G  H  ");
    }


    private String printSquare(ChessPiece piece) {
        if (piece != null) {
            String color = piece.getTeamColor().toString();
            switch (piece.getPieceType().toString()) {
                case "PAWN":
                    return color.equals("WHITE") ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
                case "KNIGHT":
                    return color.equals("WHITE") ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
                case "BISHOP":
                    return color.equals("WHITE") ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
                case "ROOK":
                    return color.equals("WHITE") ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
                case "QUEEN":
                    return color.equals("WHITE") ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
                case "KING":
                    return color.equals("WHITE") ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            }

        }
        return EscapeSequences.EMPTY;
    }

    private void badInputLength(int expectedLength, int realLength) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED +
                "Expected " + EscapeSequences.SET_TEXT_COLOR_YELLOW +
                expectedLength + EscapeSequences.SET_TEXT_COLOR_RED +
                " arguments but got " +
                EscapeSequences.SET_TEXT_COLOR_YELLOW +
                realLength +
                EscapeSequences.RESET_TEXT_COLOR
            );
    }
}
