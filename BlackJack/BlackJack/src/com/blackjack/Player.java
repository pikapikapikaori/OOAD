package com.blackjack;

/*
玩家类
 */
public class Player {

    private double cash;     //剩余资金数
    private int loseNumber;  //输次数
    private int winNumber;   //赢次数
    private Hand playerHand;
    private int playerHandNum;

    /*
    构造函数
     */
    public Player() {
        setCash(1000);
        loseNumber = 0;
        winNumber = 0;
        playerHand = new Hand();
        playerHandNum = 0;
    }

    /*
    Post: 接受发来的一张牌
     */
    public void addPlayerHand(Card a) {
        playerHand.addHandCards(a);
        playerHandNum = playerHand.getNum();
    }

    /*
    Post: 获得手牌数组
     */
    public Card[] getPlayHand() {
        return playerHand.getHandCards();
    }

    /*
    Post: 返回手牌张数
     */
    public int getPlayerHandNum() {
        return playerHandNum;
    }

    /*
    Post: 清空手牌内容，重新开始新一局游戏
     */
    public void newRound() {
        playerHand.clearHand();
    }

    /*
    Post: 得到剩余资金数
     */
    public double getCash() {
        return cash;
    }

    /*
    Post: 设置初始资金
     */
    public void setCash(double cash) {
        this.cash = cash;
    }

    /*
    Post: 获得失败次数
     */
    public int getLose() {
        return loseNumber;
    }

    /*
    Post: 失败次数增加一次
     */
    public void plusLose() {
        loseNumber++;
    }

    /*
    Post: 获得胜利次数
     */
    public int getWin() {
        return winNumber;
    }

    /*
    Post: 胜利次数增加一次
     */
    public void plusWin() {
        winNumber++;
    }

}

