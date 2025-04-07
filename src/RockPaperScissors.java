import java.util.*;
import java.util.concurrent.TimeUnit;

public final class RockPaperScissors
{
    private final RealPlayer realPlayer   = new RealPlayer("realPlayer");
    private final AiPlayer aiPlayer       = new AiPlayer("aiPlayer");

    private final Scanner scanner = new Scanner(System.in);

    private final Map<Integer, Types> types = Map.of (
            0, Types.PAPER,
            1, Types.ROCK,
            2, Types.SCISSORS
    );

    private final Map<Types, Types> wins = Map.of(
            Types.PAPER,    Types.ROCK,
            Types.ROCK,    Types.SCISSORS,
            Types.SCISSORS, Types.PAPER
    );

    private void game() {
        realPlayer.startMove();
        aiPlayer.startMove();
        System.out.println();

        realPlayer.choosing();
        aiPlayer.choosing();

        if (!checkWin(realPlayer, aiPlayer) && !checkWin(aiPlayer, realPlayer)) {
            System.out.println("Nobody won");

            waitForASecond();

            clearConsole();
        }
    }

    void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private boolean checkWin(Player player, Player against)
    {
        if (wins.get(player.currentType).equals(against.currentType)) {
            player.win();
            waitForASecond();
            clearConsole();

            return true;
        }
        return false;
    }

    private void waitForASecond()
    {
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private class AiPlayer extends Player
    {
        @Override
        void choosing() {
            Random rand = new Random();
            int randomChoice = rand.nextInt(3);

            this.currentType = types.get(randomChoice);
        }

        AiPlayer(String name) {
            this.name = name;
        }
    }

    private class RealPlayer extends Player
    {
        @Override
        void choosing() {
            RockPaperScissors ssp = new RockPaperScissors();

            System.out.println("Choose:\n0 - paper\n1 - rock\n2 - scissors\n3 - exit");

            int choice = scanner.nextInt();

            if (choice == 0 || choice == 1 || choice == 2) {
                this.currentType = types.get(choice);
            }
            else if (choice == 3) {
                clearConsole();
                System.out.println("Results:\n\n" + realPlayer + "\n" + aiPlayer);

                System.exit(0);
            }
            else {
                System.out.println("Wrong input");

                ssp.waitForASecond();
                clearConsole();

                main(new String[]{});
            }
        }

        RealPlayer(String name) {
            this.name = name;
        }
    }

    private abstract class Player
    {
        int points;
        String name;
        Types currentType;

        void startMove() {
            currentType = null;
            System.out.println(this);
        }

        void win() {
            this.points += 1;

            System.out.println(this.name + " wins!");

            new RockPaperScissors();
        }

        public String toString() {
            return this.name + " points: " + this.points;
        }

        abstract void choosing();
    }

    private enum Types
    {
        ROCK, PAPER, SCISSORS;
    }

    private RockPaperScissors() {
    }

    public static void main(String[] args) {
        RockPaperScissors ssp = new RockPaperScissors();

        ssp.clearConsole();

        while (true) {
            ssp.game();
        }
    }
}