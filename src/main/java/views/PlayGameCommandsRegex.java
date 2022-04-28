package views;

public enum PlayGameCommandsRegex {
    SHOW_MAP("show map(?: --iCoordinate (?<iCoordinate>\\d+)()| --jCoordinate (?<jCoordinate>\\d+)()){2}\\2\\4"),
    END_GAME("end game"),
    SHOW_MENU("menu show-current"),
    END_TURN("end turn"),
    endTurn("end turn"),
    MOVE_COMBAT_UNIT("move combat unit(?: -i1 (?<indexStartI>\\d+)()| -j1 (?<indexStartJ>\\d+)()" +
            "| -i2 (?<indexEndI>\\d+)()| -j2 (?<indexEndJ>\\d+)()){4}\\2\\4\\6\\8"),
    MOVE_CIVILIAN("move civilian(?: -i1 (?<indexStartI>\\d+)()| -j1 (?<indexStartJ>\\d+)()" +
            "| -i2 (?<indexEndI>\\d+)()| -j2 (?<indexEndJ>\\d+)()){4}\\2\\4\\6\\8"),
    END("end"),
    LEFT("left (?<moveCount>\\d+)"),
    UP("up (?<moveCount>\\d+)"),
    RIGHT("right (?<moveCount>\\d+)"),
    DOWN("down (?<moveCount>\\d+)"),
    SELECT_SETTLER("select settler(?: --iCoordinate (?<iCoordinate>\\d+)()| --jCoordinate (?<jCoordinate>\\d+)()){2}\\2\\4"),
    CREATE_CITY("create city"),
    ENTER_TECHNOLOGY_MENU("technology info"),
    ;
    private String regex;

    PlayGameCommandsRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String toString() {
        return this.regex;
    }
}