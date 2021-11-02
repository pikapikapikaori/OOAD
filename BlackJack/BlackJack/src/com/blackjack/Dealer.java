package com.blackjack;

/*
庄家类
 */
public class Dealer {

    private Hand dealerHand;
    private int dealerHandNum;

    /*
    构造函数
     */
    public Dealer() {
        dealerHand = new Hand();
        dealerHandNum = 0;
    }

    /*
    Post: 接受发来的一张牌
     */
    public void addDealerHand(Card a) {
        dealerHand.addHandCards(a);
        dealerHandNum = dealerHand.getNum();
    }

    /*
    Post: 返回手牌的数组
     */
    public Card[] getDealerHand() {
        return dealerHand.getHandCards();
    }

    /*
    Post: 返回手牌张数
     */
    public int getDealerHandNum() {
        return dealerHandNum;
    }

    /*
    Post: 清空手牌内容，重新开始新一局游戏
     */
    public void newRound() {
        dealerHand.clearHand();
    }


}

