package adrenaline.client.view.cli;


import adrenaline.Color;
import adrenaline.client.controller.GameController;
import adrenaline.client.model.Player;
import org.fusesource.jansi.Ansi;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fusesource.jansi.Ansi.ansi;


/**
 *
 * The abstract class of Cli,for control Cli flow
 *
 */
public abstract class ControllerCli{

    protected GameController gameController;
    protected AtomicInteger returnValueIsOk = new AtomicInteger(0); //0:initial 1:/OK 2:other
    protected Scanner scanner = new Scanner(System.in);


    /**
     *
     * For initial set of current stage
     *
     */
    protected abstract void initialStageCli();


    /**
     *
     * A static method, can print file from "src/main/resources/ForCli/"
     *
     * @param srcFileName input a source file name in "src/main/resources/ForCli/"
     *
     */
    protected static void printSrcFile(String srcFileName){
        try(InputStreamReader fileReader = new InputStreamReader(GameController.class.getResourceAsStream("/ForCli/" + srcFileName))) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String string = bufferedReader.readLine();
            while (string != null){
                printAString("ansi",string);
                string=bufferedReader.readLine();
            }
        }catch (FileNotFoundException | NullPointerException e){
            System.err.println("\n/ForCli/"+srcFileName+"  File Not Found ");
        }catch (IOException e){
            System.err.println("\n printSrcFile IOException ");
        }
    }

    /**
     *
     * A synchronized method, can read a number from console by down and up range
     *
     * @param down The down number
     * @param up The up number
     *
     */
    protected synchronized int readANumber(int down,int up) {

        int num;
        try{
            num = scanner.nextInt();
            scanner.nextLine();
        }catch (InputMismatchException e){
            System.out.println("\nPlease answer with a number");
            isQuit(scanner.nextLine());
            num = readANumber(down,up);
        }

        if (num < down || num > up) {
            System.err.println("Please answer with a number from "+down+" to "+up+"! Retry:");
            num = readANumber(down, up);
        }

        return num;
    }

    /**
     *
     * A synchronized method, can read a number from console by a range of ArrayList
     *
     * @param rangeList The range ArrayList
     *
     */
    protected synchronized int readANumber(ArrayList<Integer> rangeList){


        int num;

        try{
            num = scanner.nextInt();
            scanner.nextLine();
        }catch (InputMismatchException e){
            System.out.println("\nPlease answer with a number");
            isQuit(scanner.nextLine());
            num = readANumber(rangeList);
        }

        if (!rangeList.contains(num) && num!=-1) {
            System.err.println(num+" is invalid number, Retry:");
            num = readANumber(rangeList);
        }

        return num;

    }




    /**
     *
     * A synchronized static class,Can print a String in the console
     *  
     * @param printString input the string which you want to print
     *
     */
    protected synchronized static void printAString(String printString) {

        printAString("out", printString);

    }


    /**
     *
     * A synchronized static class,Can print a String in the console
     *
     * @param printState input a print state,
     *                   choose from err/OutWithOutNewLine/ansi or normal type
     * @param printString input the string which you want to print
     *
     */
    protected synchronized static void printAString(String printState,String printString){

        switch (printState) {
            case "err":
                System.err.println(printString);
                break;
            case "OutWithOutNewLine":
                System.out.print(printString);
                break;
            case "ansi":
                System.out.println(ansi().render(printString));
                break;
            default:
                System.out.println(printString);
                break;
        }

    }


    /**
     *
     * A synchronized class,Can read a string from console
     *
     * @return return a string readied from console
     *
     */
    protected synchronized String readAString(){

        String input = scanner.nextLine();
        isQuit(input);
        return input;

    }



    /**
     *
     * A listener to listen the return from server
     *
     * @return return a boolean value Ok return true,
     *          other return false
     *
     */
    protected boolean listenerReturnValueIsOK() {


        while (true) {
            if (returnValueIsOk.get()!=0)
                break;
        }


        if (returnValueIsOk.get()==1){
            returnValueIsOk.set(0);
            return true;
        }
        else {
            returnValueIsOk.set(0);
            return false;
        }

    }



    /**
     *
     * For print player's info
     *
     */
    protected void printLobbyInfo() {

        int num=1;

        System.out.println("\nThese players are in the lobby:");
        for (Map.Entry<String, Color> players : gameController.getPlayersNicknames().entrySet()) {

            Ansi.Color ansiColor = transferColorToAnsiColor(players.getValue());

            System.out.println(ansi().bold().fg(ansiColor).a(num+"."+players.getKey()));

            num++;
        }
    }



    /**
     *
     * Can getProperyChangeListeners the current player's turn number
     *
     * @return the turn number
     *
     */
    protected int getPlayerTurnNumber(){

        int num=1;

        for (Map.Entry<String, Color> players : gameController.getPlayersNicknames().entrySet()) {

            if (players.getKey().equals(gameController.getOwnNickname()))
                break;

            num++;
        }
        return num;
    }


    /**
     *
     * Can transfer the adrenaline.Color to Ansi.Color
     *
     * @param value The adrenaline.Color
     *
     * @return The Ansi.Color
     *
     */
    protected static Ansi.Color transferColorToAnsiColor(Color value) {


        switch (value) {
            case YELLOW:
                return Ansi.Color.YELLOW;
            case BLUE:
                return Ansi.Color.BLUE;
            case PURPLE:
                return Ansi.Color.MAGENTA;
            case GRAY:
                return Ansi.Color.WHITE;
            case GREEN:
                return Ansi.Color.GREEN;
            case RED:
                return Ansi.Color.RED;
            case WHITE:
                return Ansi.Color.BLACK;
            default:
                return Ansi.Color.DEFAULT;
        }
    }


    /**
     *
     * A synchronized class for print map Info
     *
     */
    protected synchronized void printMap() {

        System.out.println("\n-------");
        System.out.println("Map...");
        printSrcFile("Map"+gameController.getMap().getMapID()+".txt");

    }


    /**
     *
     * A synchronized class for print map Info
     *
     */
    protected synchronized void printMapAndMapWeaponAmmoInfo() {


        adrenaline.client.model.Map map = gameController.getMap();


        printMap();

        System.out.println("\n---------------");
        System.out.println("Weapon Info...");
        System.out.println(" ");
        System.out.println(ansi().bold().fg(Ansi.Color.BLUE).a("At map square  NO.2 █").fgDefault());
        printWeaponInfo(map.getWeaponMap().get(Color.BLUE));
        System.out.println(" ");
        System.out.println(ansi().bold().fg(Ansi.Color.RED).a("At map square  NO.4 █").fgDefault());
        printWeaponInfo(map.getWeaponMap().get(Color.RED));
        System.out.println(" ");
        System.out.println(ansi().bold().fg(Ansi.Color.YELLOW).a("At map square  NO.11 █").fgDefault());
        printWeaponInfo(map.getWeaponMap().get(Color.YELLOW));


        printMapAmmoCardInfo(map.getAmmoMap());




    }


    /**
     *
     * For print ammo box in the map square
     *
     * @param ammoMap The ammoMap reference
     */
    private void printMapAmmoCardInfo(HashMap<Integer, Integer> ammoMap) {

        System.out.println("\n--------------Ammo Box------------");

        for (Map.Entry<Integer, Integer> ammo : ammoMap.entrySet()){

            System.out.print(ammo.getKey()+": ");
            printSrcFile("AmmoBox"+ammo.getValue()+".txt");

        }

    }


    /**
     *
     * A synchronized class for print players' Info
     *
     */
    protected synchronized void printPlayerStateInfo(){


        Set<Map.Entry<String, Color>> entryNicknameMap = gameController.getPlayersNicknames().entrySet();

        for (Map.Entry<String, Color> player : entryNicknameMap){

            Player modelPlayer = gameController.getPlayersMap().get(player.getValue());


            System.out.println("----------------");
            System.out.print("--->");
            System.out.println(ansi().bold().
                    fg(transferColorToAnsiColor(player.getValue())).a(player.getKey()).fgDefault());

            System.out.println("Position: "+modelPlayer.getPosition());
            System.out.print("AmmoBox: ");
            printPlayerAmmoBoxInfo(modelPlayer.getAmmoBox());

            printDamageTrack(modelPlayer.getDamage());
            printMarkTrack(modelPlayer.getMarks());


        }

    }

    /**
     * For print mark track
     * @param marks makes track ArrayList
     */
    private synchronized static void printMarkTrack(ArrayList<Color> marks) {

        System.out.print("Mark: ");
        for (Color color:marks){
            System.out.print(ansi().fg(transferColorToAnsiColor(color)).a("♠ ").fgDefault());
        }
        System.out.println(" ");

    }

    /**
     *
     * For print damage track
     * @param damageArrayList damage ArrayList
     */
    private synchronized static void printDamageTrack(ArrayList<Color> damageArrayList) {

        System.out.print("Damage: ");
        for (Color color:damageArrayList){
            System.out.print(ansi().fg(transferColorToAnsiColor(color)).a("❤ ").fgDefault());
        }
        System.out.println(" ");
    }


    /**
     *
     * A synchronized class for print Ammo BoxInfo
     * @param ammoBox ammobox array seq:r b y p
     */
    private synchronized void printPlayerAmmoBoxInfo(int[] ammoBox) {



        for (int i=0;i<=2;i++){
            for (int j=ammoBox[i];j>0;j--){
                switch (i) {
                    case 0:
                        System.out.print(ansi().fg(Ansi.Color.RED).a("█ ").fgDefault());
                        break;
                    case 1:
                        System.out.print(ansi().fg(Ansi.Color.BLUE).a("█ ").fgDefault());
                        break;
                    case 2:
                        System.out.print(ansi().fg(Ansi.Color.YELLOW).a("█ ").fgDefault());
                        break;
                        default:
                }
            }
        }

        System.out.println(" ");

    }


    /**
     *
     * A static class,For print powerup cards' info.
     *
     * @param powerupList The list contain the powerup card which have to print
     *
     */
    protected static void printPowerupInfo(ArrayList<Integer> powerupList){

        if (powerupList.size()==0) {
            System.err.println("You don't have powerup card now");
            return;
        }

        for (int powerup:powerupList){
            printSrcFile("Powerup"+powerup+".txt");
        }

    }


    /**
     *
     * For print player own powerup and weapon cards
     *
     */
    protected synchronized void printPlayerOwnPowerupAndWeaponInfo() {

        Player player = gameController.getPlayersMap().get(gameController.getOwnColor());

        System.out.println("\n-------");
        System.out.println("Your own powerup cards...");
        printPowerupInfo(player.getPowerupCards());
        System.out.println("Your own weapon cards...");
        printWeaponInfo(player.getWeaponCards());

    }



    /**
     *
     * A static class,For print weapon cards' info.
     *
     * @param weaponList The list contain the weapon card which have to print
     *
     */
    protected static void printWeaponInfo(ArrayList<Integer> weaponList){

        if (weaponList.size()==0) {
            System.err.println("Don't have weapon card now");
            return;
        }

        for (int weapon:weaponList){
            printSrcFile("Weapon"+weapon+".txt");
        }

    }

    /**
     *
     * For print Ranking
     *
     */
    protected void printFinalRanking(){

        String winPlayer = " ";


        Set<Map.Entry<String, Color>> entryNicknameMap = gameController.getPlayersNicknames().entrySet();

        System.out.println("------------------------Final Ranking------------------------");

        for (Map.Entry<String, Color> player : entryNicknameMap) {

            Map<Color, Integer> rankingPlayer = gameController.getScoreBoard().getFinalPlayersPosition();

            System.out.print("|--->");
            System.out.println(ansi().bold().
                    fg(transferColorToAnsiColor(player.getValue())).a(player.getKey()).fgDefault()+" : "+
                            rankingPlayer.get(player.getValue()));

            try {
                if (rankingPlayer.get(player.getValue())==1) {
                    winPlayer=player.getKey();

                }
            }catch (NullPointerException ignore){

            }


        }


        System.out.println("\n-------->");
        System.out.println(ansi().bold().
                fg(transferColorToAnsiColor(gameController.getPlayersNicknames().get(winPlayer)))
                    .a(winPlayer).fgDefault()+ " is final winner!!! Congratulation!!! ");


    }

    /**
     *
     *
     * For print score.
     *
     */
    protected void printScore(){

        Set<Map.Entry<String, Color>> entryNicknameMap = gameController.getPlayersNicknames().entrySet();

        System.out.println("------------------------Score------------------------");
        for (Map.Entry<String, Color> player : entryNicknameMap) {

            Map<Color, Integer> scorePlayer = gameController.getScoreBoard().getScoreMap();

            System.out.print("--->");
            System.out.println(ansi().bold().
                    fg(transferColorToAnsiColor(player.getValue())).a(player.getKey()).fgDefault()+" : "+
                    scorePlayer.get(player.getValue()));

        }


    }


    /**
     *
     * For exit the game
     *
     * @param input If input string is "Quit",the game will exit
     *
     */
    protected void isQuit(String input){

        if (input.equals("Quit")){
            System.out.println("Ok, Game Exit, Thank you for your participation!");
            gameController.cleanExit();
            System.exit(0);
        }

    }
}

