package com.blackjack;

/*
牌组类
 */
public class Deck {

    private Card cards[] = new Card[52];

    /*
    构造函数
     */
    public Deck() {
        for(int i = 0; i < 52; i++){
            cards[i] = new Card(i+1);  //new Card(i+1)
        }
    }

    /*
    Post: 洗牌，返回卡组
     */
    public Card[] shuffle() {
        Card afterShuffle[] = new Card[52];

        for (int i = 0; i < 52; i++) {
            int index = (int)(Math.random() * 52);  //生成0-51之间的随机数
            afterShuffle[i] = cards[index];
            for (int j = 0; j < i; j++)
            {
                if (afterShuffle[i] == afterShuffle[j])
                {
                    i--;
                    break;
                }
            }
        }

        return afterShuffle;
    }

}

