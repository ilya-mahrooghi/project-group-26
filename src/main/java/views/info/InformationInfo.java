package views.info;

import controllers.GameControllers.GameMenuCommandController;
import models.GameMap;
import models.Player;
import models.UsersDatabase;
import views.Menu;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class InformationInfo extends Menu {
    private Player player;
    private GameMenuCommandController gameMenuCommandController;
    private GameMap gamemap;
    private ArrayList<Player> players;

    public InformationInfo(UsersDatabase usersDatabase, Player player, GameMenuCommandController gameMenuCommandController,
                           ArrayList<Player> players, GameMap gamemap) {
        super(usersDatabase);
        this.player = player;
        this.gameMenuCommandController = gameMenuCommandController;
        this.players = players;
        this.gamemap = gamemap;
    }

    @Override
    public void run() {
        String input;
        Matcher matcher;
        while (true) {
            input = scanner.nextLine();
            if ((matcher = getCommandMatcher(input, InformationInfoEnum.EXIT.toString())) != null) {
                return;
            } else if ((matcher = getCommandMatcher(input, InformationInfoEnum.SHOW_INFORMATION.toString())) != null) {
            } else {
                System.out.println("invalid command!");
            }
        }
    }
}