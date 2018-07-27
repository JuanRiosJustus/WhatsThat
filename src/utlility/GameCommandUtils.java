package utlility;

import responsesRequests.GameStartResponse;
import messaging.Message;
import dialogs.Constants;

import java.util.Arrays;

public class GameCommandUtils {

    private GameCommandUtils() { /* NO INSTANTIATION */ }

    /**
     * Determines if the given message is a correctly formatted game start command where
     * STRING_COMMAND_TOKEN(0) STRING_COMMAND_NAME(1) STRING_TOPIC(2) INT_ROUNDS(3)
     * @param msg
     * @return
     */
    public static GameStartResponse isCorrectlyFormattedGameStartCommand(Message msg) {
        System.out.println( "FULL MESSAGE: " + msg.build() );
        String[] tokens = msg.getContent().split("\\s+");
        System.out.println( Arrays.toString(tokens) );

        if (tokens.length != Constants.COMMAND_GAMESTART_PARAMETERS) {
            return new GameStartResponse(msg.getAuthor(), "NOT SUFFICIENT AMOUNT OF PARAMS", null, false);
        }

        if (!StringUtils.isNumeric(tokens[3])) {
            return new GameStartResponse(msg.getAuthor(), "ROUNDS MUST BE INTEGER TYPE", null, false);
        }

        if (tokens[3].length() >= Integer.MAX_VALUE / 2){
            return new GameStartResponse(msg.getAuthor(), "TO MANY INTEGERS FOR ROUNDS", null, false);
        }
        if (Integer.valueOf(tokens[3]) < 1) {
            return new GameStartResponse(msg.getAuthor(), "ROUNDS MUST BE POSITIVE", null, false);
        }

        return new GameStartResponse(msg.getAuthor(), "GAMESTART COMMAND SUCCESSFUL", null, true);
    }


}
