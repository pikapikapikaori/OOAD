package com.blackjack;

import java.util.Scanner;

/*
BlackjackGame控制类
 */
public class BlackjackGame {

    private Dealer dealer;  //庄家
    private Player player;  //玩家
    private Deck deck;      //一副牌
    private int command;    //输入的命令
    Scanner s;

    private Card [] cards;  //洗好的一副牌的数组
    private int pNum;       //玩家手上的牌数
    private int dNum;       //庄家手上的牌数
    private int num;        //记录目前发到这副牌的第几张
    private Card [] pHand;  //玩家手牌数组
    private Card [] dHand;  //庄家手牌数组

    /*
    构造函数
     */
    public BlackjackGame() {
        dealer = new Dealer();
        player = new Player();
        deck = new Deck();
        command = 0;
        s = new Scanner(System.in);

        while (true) {
            dealer.newRound();  //清洗庄家手上的牌，开始新的一轮
            player.newRound();  //清洗玩家手上的牌，开始新的一轮
            System.out.println("----------BLACKJACK GAME----------");
            System.out.println("1.START A NEW ROUND");
            System.out.println("2.BALANCE");
            System.out.println("3.GAME RECORD");
            System.out.println("4.EXIT THE GAME");
            System.out.println("----------------------------------");
            System.out.println("Please input your choice(1 or 2 or 3 or 4)：");

            try{
                command = s.nextInt();
            }catch(Exception e) {
                e.printStackTrace();
            }

            switch(command) {
                case 1: start(); break;
                case 2: checkBalance(); break;
                case 3: checkRecord(); break;
                case 4: System.exit(0); break;
                default: System.out.println("Please input 1 or 2 or 3 or 4!");
            }
        }
    }

    /*
    Post: 开始新的一轮游戏
     */
    public void start() {

        cards = new Card[52];  //一共有52张牌，没有大小王
        pNum = 0;             //玩家手上的牌数
        dNum = 0;             //庄家手上的牌数
        num = 0;              //记录目前发到这副牌的第几张

        for (int i = 0; i < 52; i++) cards[i] = new Card();
        cards = deck.shuffle();  //洗牌

        double balance = player.getCash();
        do {
            System.out.println("Please input your bet:");
            try{
                command = s.nextInt();
            }catch(Exception e) {
                e.printStackTrace();
            }

            if(command <= 0) {
                System.out.println("You cannot input a negative bet!\n");
            }
            else if( command > balance) {
                System.out.println("You don't have enough money!\n");
            }
        }while(command <= 0 || command > balance);

        double bet = command;  //投注金额

        System.out.println("----------DEAL PLAYER CARDS----------");  //开始发牌

        pHand = new Card[5];  //玩家手牌
        dHand = new Card[5];  //庄家手牌

        dealPlayerCard(bet);  //给玩家发牌
    }

    /*
    Post: 查看余额
     */
    public void checkBalance() {
        System.out.println("BALANCE: " + player.getCash() + "\n");
    }

    /*
    Post: 查看胜负记录
     */
    public void checkRecord() {
        System.out.println("WIN: " + player.getWin());
        System.out.println("LOSE: " + player.getLose());
    }

    /*
    Post: 发牌
     */
    public void dealPlayerCard(double bet) {

        do
        {
            player.addPlayerHand(cards[num++]);  //给玩家发一张牌

            //显示玩家得到的牌
            pHand = player.getPlayHand();      //玩家手牌
            pNum = player.getPlayerHandNum();  //玩家手牌张数
            String str;
            int nowPoint = 0, nowAnum = 0;
            System.out.print("You have:  ");
            for(int i = 0; i < pNum; i++) {
                str = pHand[i].getSuit();
                str = str + " " + pHand[i].getFace();
                int nowP = pHand[i].getPoint();
                if(nowP == 11) nowAnum++;
                nowPoint += nowP;
                System.out.print(str + "  ");
            }
            //如果手上有A，并且手牌点数超过21点，则A被认为是1点
            while(nowPoint > 21 && nowAnum > 0) {
                nowPoint -= 10;
                nowAnum--;
            }
            System.out.println();

            //如果玩家当前手牌点数不超过21点，输出玩家点数。
            if(nowPoint <= 21){
                if(nowPoint == 21 && pNum == 2) {
                    System.out.println("Your point: black jack");
                }
                else System.out.println("Your point: "+ nowPoint);
            }
            //否则，玩家爆掉直接开牌结束游戏。
            else {
                System.out.println("Your point: bust");
                gameResult(bet);
                command = 1;
                break;
            }

            //得到用户输入
            System.out.println("Your choice: 1.Back;  2.Hit;  3.Stand; ");
            try{
                command = s.nextInt();
            }catch(Exception e) {
                e.printStackTrace();
            }

            switch(command) {
                case 1: player.setCash(player.getCash() - bet); break;  //返回首页菜单，退出该局游戏，用时损失1倍赌注
                case 2: dealPlayerCard(bet); break;  //再要一张牌
                case 3: gameResult(bet); command = 1; break;  //开牌,调用函数之后结束该局游戏。
                default:System.out.println("You have to input 1 or 2 or 3!");
            }

        }while(command != 1 && num <= 5);  //发牌数不超过5张
    }

    /*
    Pre: 玩家决定开牌
    Post: 显示游戏结果，开始先各自计算玩家和庄家的点数和
     */
    public void gameResult(double bet) {
        int pPoint = totalPPoint();
        int dPoint = totalDPoint();
        double nowCash = player.getCash();
        System.out.println("----------RESULT OF THIS ROUND----------");
        //判断输赢,决定玩家资金的变化
        System.out.print("YOU: ");
        if(pPoint > 21) {
            System.out.print("bust   DEALER: ");
            if(dPoint == 21 && dNum == 2) {
                System.out.print("black jack\n");
                player.setCash(nowCash - 1.5 * bet);  //输1.5倍赌注
            }
            else {
                System.out.println(dPoint);
                player.setCash(nowCash - bet);  //输1倍赌注
            }
            System.out.println("YOU LOSE!");
            player.plusLose();
        }
        else if(pPoint < 21) {
            System.out.print(pPoint + "   DEALER: ");
            if(dPoint == 21 && dNum == 2) {
                System.out.print("black jack\nYOU LOSE!\n");
                player.setCash(nowCash - 2 * bet);  //输1.5倍
                player.plusLose();
            }
            else if(dPoint > 21) {
                System.out.print("bust\nYOU WIN!\n");
                player.setCash(nowCash + bet);  //赢1倍
                player.plusWin();
            }
            else {
                System.out.println(dPoint);
                if(pPoint < dPoint) {
                    System.out.println("YOU LOSE!");
                    player.setCash(nowCash - bet);  //输1倍
                    player.plusLose();
                }
                else if(pPoint > dPoint) {
                    System.out.println("YOU WIN!");
                    player.setCash(nowCash + bet);  //赢1倍
                    player.plusWin();
                }
                else System.out.println("DRAW!");  //平局
            }
        }
        //pPoint == 21
        else {
            if(pNum == 2) {
                System.out.print("black jack   DEALER: ");
                if(dPoint == 21 && dNum == 2)
                    System.out.print("black jack\nDRAW!\n");
                else if(dPoint > 21) {
                    System.out.print("bust\nYOU WIN!\n");
                    player.setCash(nowCash + 1.5 * bet);  //赢1.5倍
                    player.plusWin();
                }
                else {
                    System.out.print(dPoint+"\nYOU WIN!");
                    player.setCash(nowCash + 1.5 * bet);  //赢1.5倍
                    player.plusWin();
                }
            }
            else {
                System.out.print(pPoint+"   DEALER: ");
                if(dPoint == 21 && dNum == 2){
                    System.out.print("black jack\nYOU LOSE!\n");
                    player.setCash(nowCash - 1.5 * bet);  //输1.5倍
                    player.plusLose();
                }
                else if(dPoint == 21 && dNum > 2)
                    System.out.print(dPoint+"\nDRAW!\n");
                else if(dPoint > 21) {
                    System.out.print("bust\nYOU WIN!\n");
                    player.setCash(nowCash + bet);  //赢1倍
                    player.plusWin();
                }
                else {
                    System.out.print(dPoint+"\nYOU WIN!");
                    player.setCash(nowCash + bet);  //赢1倍
                    player.plusWin();
                }
            }
        }
    }

    /*
    Post:返回最后玩家的点数
     */
    public int totalPPoint() {
        int pPoint = 0, aNum = 0;

        for (int i = 0; i < pNum; i++) {
            int getPoint = pHand[i].getPoint();
            if(getPoint == 11) aNum++;
            pPoint += getPoint;
        }
        while(pPoint > 21 && aNum > 0) {
            pPoint -= 10;
            aNum--;
        }
        return pPoint;
    }

    /*
    Post: 计算庄家的所有发牌以及最后庄家点数
     */
    public int totalDPoint() {
        int dPoint = 0, anum =0;  //anum存储纸牌A出现的次数
        System.out.println("----------DEAL DEALER CARDS----------"); //给庄家发牌
        String str;

        do {
            Card nowCard = new Card();
            nowCard = cards[num++];
            dealer.addDealerHand(nowCard);     //发给庄家一张牌
            dHand = dealer.getDealerHand();    //庄家手牌数组
            dNum = dealer.getDealerHandNum();  //庄家手牌张数

            System.out.print("Dealer has:  ");
            for(int i = 0; i < dNum; i++) {
                str = dHand[i].getSuit();
                str = str + " " + dHand[i].getFace();
                System.out.print(str + "  ");
            }
            System.out.println();


            int gPoint = nowCard.getPoint();
            dPoint += gPoint;
            if(gPoint == 11) anum++;
            while (dPoint > 21 && anum > 0) {
                dPoint -= 10;
                anum--;
            }

        }while(dPoint <= 17 && dNum <= 5);  //庄家手牌超过17点就不再要牌，同时要满足只发5张牌的限制

        return dPoint;
    }
}


