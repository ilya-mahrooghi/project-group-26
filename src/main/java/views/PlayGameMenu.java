package views;

import controllers.GameControllers.GameMenuCommandController;
import controllers.GameControllers.PlayGameMenuController;
import controllers.GameControllers.ShowMapController;
import models.*;
import controllers.*;
import models.Tile.Tile;
import models.Units.Nonecombat.NoneCombatUnits;
import views.info.TechnologyInfo;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class PlayGameMenu extends Menu {
    // TODO : از قصد فرندلیه ؟
    ArrayList<Player> players;
    GameMap gamemap;
    ShowMapController showMapController;
    GameMenuCommandController gameMenuCommandController;
    PlayGameMenuController playGameMenuController;

    public PlayGameMenu(ArrayList<Player> players, UsersDatabase usersDatabase) {
        super(usersDatabase);
        this.players = players;
        gamemap = new GameMap(this.players);
        this.showMapController = new ShowMapController(gamemap, players);
        playGameMenuController = new PlayGameMenuController(gamemap, players);
        gameMenuCommandController = new GameMenuCommandController(playGameMenuController);
        playGameMenuController.startGame(players);
    }

    @Override
    public void run() {
        String input;
        int playerNumber = 0;
        while (true) {
            players.get(playerNumber).updateMap(gamemap);
            Matcher matcher;
            input = super.scanner.nextLine();
            if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.SHOW_MAP.toString())) != null) {
                showMapCommand(matcher, playerNumber);
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.END_GAME.toString())) != null) {
                // TODO : add json command
                return;
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.SHOW_MENU.toString())) != null) {
                System.out.println("Game Menu");
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.END_TURN.toString())) != null) {
                //TODO :  reset functions ?!?
                players.get(playerNumber).endTurn(this.gamemap);
                playerNumber = playGameMenuController.nextPlayer(playerNumber, this.players);
//                playGameMenuController.setupTurn(this.players.get(playerNumber)); // for now
                // TODO : setup every thing for the next player !
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.MOVE_COMBAT_UNIT.toString())) != null) {
                System.out.println(gameMenuCommandController.moveCombatUnit(matcher, gamemap, players.get(playerNumber)));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.MOVE_CIVILIAN.toString())) != null) {
                System.out.println(gameMenuCommandController.moveCivilian(matcher, gamemap, players.get(playerNumber)));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.SHOW_MAP_BY_CITY.toString())) != null) {
                showMapByCity(matcher, players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.SELECT_SETTLER.toString())) != null) {
                selectSettler(matcher, players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.BUILD_IN_CITY.toString())) != null) {
                System.out.println(gameMenuCommandController.buildInCity(matcher, players.get(playerNumber),false));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.INSTANT_BUILD_IN_CITY.toString())) != null) {
                System.out.println(gameMenuCommandController.buildInCity(matcher, players.get(playerNumber),true));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.ENTER_TECHNOLOGY_MENU.toString())) != null) {
                technologyInfo(players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.INCREASE_TURN.toString())) != null) {
                gameMenuCommandController.increaseTurn(matcher, players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.INCREASE_FOOD.toString())) != null) {
                gameMenuCommandController.increaseFood(matcher, players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.INCREASE_HAPPINESS.toString())) != null) {
                gameMenuCommandController.increaseHappiness(matcher, players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.INCREASE_GOLD.toString())) != null) {
                gameMenuCommandController.increaseGold(matcher, players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.SHOW_GOLD.toString())) != null) {
                showGold(players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.SHOW_HAPPINESS.toString())) != null) {
                showHappiness(players.get(playerNumber));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.SHOW_CITY_FOOD.toString())) != null) {
                showCityFood(players.get(playerNumber), matcher);
            } else {
                System.out.println("invalid command!");
            }
        }
    }

    public void showMap(int iCoordinate, int jCoordinate, int playerNumber) {
        Player player = this.players.get(playerNumber);
        player.updateMap(this.gamemap);
        Tile[][] tilesToShow = new Tile[3][6];
        this.showMapController.setTileArrayToPrint(iCoordinate, jCoordinate, tilesToShow, player.getGameMap().getMap());
        String[][] toPrint = new String[80][80];
        this.showMapController.setToPrintStrings(toPrint, tilesToShow, iCoordinate, jCoordinate, playerNumber);
        for (int i = 0; i <= 21; i++) {
            for (int j = 0; j < 51; j++) {
                System.out.print(toPrint[i][j]);
            }
            System.out.println();
        }
    }

    public void showMapCommand(Matcher matcher, int playerNumber) {
        Output output = this.gameMenuCommandController.showMap(matcher);
        if (output != null) {
            System.out.println(output.toString());
            return;
        } else {
            int iCoordinate = Integer.parseInt(matcher.group("iCoordinate"));
            int jCoordinate = Integer.parseInt(matcher.group("jCoordinate"));
            showMap(iCoordinate, jCoordinate, playerNumber);
            changeDirection(iCoordinate, jCoordinate, playerNumber);
        }
    }

    public void showMapByCity(Matcher matcher, Player player) {
        Output output = this.gameMenuCommandController.showMapByCity(matcher, player);
        if (output != null) {
            System.out.println(output);
            return;
        }
        showMapByCity(player, player.getCityByName(matcher.group("cityName")));
    }

    private void showMapByCity(Player player, City city) {
        int iCoordinate = this.gamemap.getIndexI(city.getCenter());
        int jCoordinate = this.gamemap.getIndexJ(city.getCenter());
        showMap(iCoordinate - 1, jCoordinate - 2, players.indexOf(player));
        changeDirection(iCoordinate, jCoordinate, players.indexOf(player));
    }

    private void changeDirection(int iCoordinate, int jCoordinate, int playerNumber) {
        String input;
        while (true) {
            input = super.scanner.nextLine();
            Matcher matcher;
            if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.LEFT.toString())) != null) {
                int moveCount = -(Integer.parseInt(matcher.group("moveCount")));
                changeDirectionHorizontal(iCoordinate, jCoordinate, playerNumber, moveCount);
                if (gameMenuCommandController.changeShowMapDirection(iCoordinate, jCoordinate + moveCount) == null)
                    jCoordinate += moveCount;
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.DOWN.toString())) != null) {
                int moveCount = Integer.parseInt(matcher.group("moveCount"));
                changeDirectionVertical(iCoordinate, jCoordinate, playerNumber, moveCount);
                if (gameMenuCommandController.changeShowMapDirection(iCoordinate + moveCount, jCoordinate) == null)
                    iCoordinate += moveCount;
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.RIGHT.toString())) != null) {
                int moveCount = Integer.parseInt(matcher.group("moveCount"));
                changeDirectionHorizontal(iCoordinate, jCoordinate, playerNumber, moveCount);
                if (gameMenuCommandController.changeShowMapDirection(iCoordinate, jCoordinate + moveCount) == null)
                    jCoordinate += moveCount;
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.UP.toString())) != null) {
                int moveCount = -(Integer.parseInt(matcher.group("moveCount")));
                changeDirectionVertical(iCoordinate, jCoordinate, playerNumber, moveCount);
                if (gameMenuCommandController.changeShowMapDirection(iCoordinate + moveCount, jCoordinate) == null)
                    iCoordinate += moveCount;
            } else if (getCommandMatcher(input, PlayGameCommandsRegex.END.toString()) != null) return;
            else System.out.println("invalid command!");
            // TODO: move Move methods to select unit command
        }
    }

    private void changeDirectionHorizontal(int iCoordinate, int jCoordinate, int playerNumber, int moveCount) {
        Output output = gameMenuCommandController.changeShowMapDirection(iCoordinate, jCoordinate + moveCount);
        if (output != null)
            System.out.println(output.toString());
        else {
            jCoordinate += moveCount;
            showMap(iCoordinate, jCoordinate, playerNumber);
        }
    }

    private void changeDirectionVertical(int iCoordinate, int jCoordinate, int playerNumber, int moveCount) {
        Output output = gameMenuCommandController.changeShowMapDirection(iCoordinate + moveCount, jCoordinate);
        if (output != null)
            System.out.println(output.toString());
        else {
            iCoordinate += moveCount;
            showMap(iCoordinate, jCoordinate, playerNumber);
        }
    }

    private void selectSettler(Matcher matcher, Player player) {
        Output output = gameMenuCommandController.selectSettler(matcher, player, this.gamemap);
        if (output != null) {
            System.out.println(output.toString());
            return;
        }
        giveCommandToSettler(matcher, player);
    }

    private void giveCommandToSettler(Matcher matcher, Player player) {
        NoneCombatUnits settler = playGameMenuController.findSettler(matcher, player);
        String input;
        while (true) {
            input = super.scanner.nextLine();
            if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.CREATE_CITY.toString())) != null) {
                Output output = gameMenuCommandController.createCity(matcher, settler, player, players);
                System.out.println(output);
                if (output == Output.CITY_CREATED)
                    return;
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.MOVE_CIVILIAN.toString())) != null) {
                System.out.println(gameMenuCommandController.moveCivilian(matcher, gamemap, player));
            } else if ((matcher = getCommandMatcher(input, PlayGameCommandsRegex.END.toString())) != null)
                return;
            else
                System.out.println("invalid command!");
        }
    }

    private void technologyInfo(Player player) {
        Output output = gameMenuCommandController.enterTechnologyInfo(player);
        if (output != null) {
            System.out.println(output);
            return;
        }
        TechnologyInfo technologyInfo = new TechnologyInfo(usersDatabase, player, gameMenuCommandController);
        technologyInfo.run();
    }

    private void showGold(Player player) {
        System.out.println("gold: " + player.getGold());
    }

    private void showHappiness(Player player) {
        System.out.println("happiness: " + player.getHappiness());
    }

    private void showCityFood(Player player, Matcher matcher) {
        Output output = gameMenuCommandController.showCityFood(matcher, player);
        if (output != null) {
            System.out.println(output);
            return;
        }
        City city = player.getCityByName(matcher.group("cityName"));
        System.out.println("city: " + city.getName() + " food: " + Food.getFoodProduction(city));
    }
}