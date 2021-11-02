//
//  Ant.h
//  Ant
//
//  Created by 李亦杨 on 2021/9/23.
//

#ifndef Ant_h
#define Ant_h

#include <math.h>

struct DefaultStickData
{
    const double leftEnd = 0;
    const double rightEnd = 300;
};

struct DefaultAntData
{
    const int antcnt = 5;
    const int GroupCnt = 32;
    const double speed = 5;
    const bool direct = true;
    const double loc[5] = {30, 80, 110, 160, 250};
};

const DefaultStickData DefaultStick;
const DefaultAntData DefaultAnt;



struct GameData
{
    bool antDirection[DefaultAnt.antcnt];
    int gameTime;
};


/*
 The Ant class.
 */
class Ant
{
private:
    int antId;
    double velocity;
    bool direction;     // True stands for right.
    double location;
    bool isAlive;       // True stands for alive.
    
public:
    Ant ();
    void setAnt (int antID, double vel, bool dir, double loc);
    
    void changeDirection ();
    void creeping ();
    bool isColllision (bool isCol);
    bool isLive ();
    void changeLive ();
    
    double getLocation ();
    double getVelocity ();
    bool getDirection ();
    
};

/*
 Post: Ant class is initialized.
 */
Ant::Ant ()
{
    
}

void Ant::setAnt (int antID, double vel, bool dir, double loc)
{
    antId = antID;
    velocity = vel;
    direction = dir;
    location = loc;
    isAlive = true;
}

/*
 Post: The ant's direction is changed.
 */
void Ant::changeDirection ()
{
    if (isAlive)
        direction = !direction;
}

/*
 The function keeps the ant creeping by 1 second.
 */
void Ant::creeping ()
{
    if (isAlive)
        if (direction == true)
            location += velocity;
        else
            location -= velocity;
}

/*
 Post: This function returns a bool value on the basis of isCol given by the detectCollision function in CreepingGame.
 */
bool Ant::isColllision (bool isCol)
{
    return isCol;
}

/*
 Post: The status of the ant is retuned.
 */
bool Ant::isLive ()
{
    return isAlive;
}

/*
 Post: The status of the ant is changed.
 */
void Ant::changeLive ()
{ 
    if (isAlive)
        isAlive = !isAlive;
}

/*
 Post: The function prints the ant's location.
 */
double Ant::getLocation ()
{
    return location;
}

/*
 Post: The function prints the ant's velocity.
 */
double Ant::getVelocity ()
{
    return velocity;
}

/*
 Post: The function prints the ant's direction.
 */
bool Ant::getDirection()
{
    return direction;
}


/*
 The Stick class.
 */
class Stick
{
private:
    double leftLocation;
    double rightLocation;
    int livingAnts;
    
public:
    Stick ();
    void setStick (double leftLoc, double rightLoc);
    
    bool isOut (Ant tmpAnt);
    
    void killAnt ();
    int getLivAntCnt ();
    
};

/*
 Post: The stick is initialized, giving the value of leftLocation is smaller than that of rightLocation.
 */
Stick::Stick ()
{
    
}

void Stick::setStick (double leftLoc, double rightLoc)
{
    leftLocation = leftLoc;
    rightLocation = rightLoc;
    livingAnts = DefaultAnt.antcnt;
}

/*
 Post: The function examines whether the given ant is out of the stick, true stands for out.
 */
bool Stick::isOut (Ant tmpAnt)
{
    if (tmpAnt.getLocation () <= leftLocation || tmpAnt.getLocation () >= rightLocation)
        return true;
    return false;
}

/*
 Post: The amount of ants decreases by 1.
 */
void Stick::killAnt ()
{
    livingAnts --;
}

/*
 Post: The amount of living ants is returned.
 */
int Stick::getLivAntCnt ()
{
    return livingAnts;
}


/*
 The CreepingGame class.
 */
class CreepingGame
{
private:
    int gameTime;
    bool isGameOver;       // True stands for not over.
    double incTim;
    Ant livant[DefaultAnt.antcnt];
    Stick curstick;
    
public:
    CreepingGame (int gameTim = 0, bool isGameOve = false, double incT = 1);
    
    void drivingGame ();
    void startGame (GameData gData);
    void detectCollision ();
    bool isAllDead ();
    
    bool getGameStatus ();
    int getGameTime ();
};

/*
 Post: The Game is initialized.
 */
CreepingGame::CreepingGame (int gameTim, bool isGameOve, double incT)
{
    gameTime = gameTim;
    isGameOver = isGameOve;
    incTim = incT;
}

/*
 Post: The game's time is increased by 1 second;
 */
void CreepingGame::drivingGame ()
{
    while (1)
    {
        gameTime += incTim;
        
        detectCollision ();
        
        for (int i = 0; i < DefaultAnt.antcnt; i++)
            if (livant[i].isLive ())
                livant[i].creeping ();
        
        for (int i = 0; i < DefaultAnt.antcnt; i++)
        {
            if (livant[i].isLive ())
                if (curstick.isOut (livant[i]))
                {
                    livant[i].changeLive ();
                    curstick.killAnt ();
                }
        }
        
        if (isAllDead () == true)
            break;
    }
    
    isGameOver = true;
}

/*
 Post: The game is started and the stick and ants are initialized.
 */
void CreepingGame::startGame (GameData gData)
{
    curstick.setStick (DefaultStick.leftEnd, DefaultStick.rightEnd);
    
    for (int i = 0; i < DefaultAnt.antcnt; i ++)
        livant[i].setAnt (i, DefaultAnt.speed, gData.antDirection[i], DefaultAnt.loc[i]);
    
}

/*
 Post: Collision are detected and ants' directions are changed.
 */
void CreepingGame::detectCollision ()
{
    int tmpantid = -1;
    for (int i = 0; i < DefaultAnt.antcnt; i ++)
    {
        tmpantid = -1;
        if (livant[i].isLive ())
        {
            if (livant[i].getDirection () == true)
            {
                for (int j = 0; j < DefaultAnt.antcnt; j ++)
                    if (livant[j].isLive ())
                    {
                        if (tmpantid == -1)
                        {
                            if (livant[j].getLocation () > livant[i].getLocation ())
                                tmpantid = j;
                        }
                        else
                        {
                            if (livant[j].getLocation () > livant[i].getLocation () && livant[j].getLocation () < livant[tmpantid].getLocation ())
                                tmpantid = j;
                        }
                    }
                if (tmpantid != -1)
                {
                    if (livant[tmpantid].getDirection() == false)
                    {
                        if (abs (livant[i].getLocation () - livant[tmpantid].getLocation ()) <= (livant[i].getVelocity () + livant[tmpantid].getVelocity ()))
                        {
                            livant[i].changeDirection ();
                            livant[tmpantid].changeDirection ();
                        }
                    }
                    else
                    {
                        if (abs (livant[i].getLocation () - livant[tmpantid].getLocation ()) <= abs (livant[i].getVelocity () - livant[tmpantid].getVelocity ()))
                        {
                            livant[i].changeDirection ();
                            livant[tmpantid].changeDirection ();
                        }
                    }
                }
            }
            else
            {
                for (int j = 0; j < DefaultAnt.antcnt; j ++)
                    if (livant[j].isLive ())
                    {
                        if (tmpantid == -1)
                        {
                            if (livant[j].getLocation () < livant[i].getLocation ())
                                tmpantid = j;
                        }
                        else
                        {
                            if (livant[j].getLocation () < livant[i].getLocation () && livant[j].getLocation () > livant[tmpantid].getLocation ())
                                tmpantid = j;
                        }
                    }
                if (tmpantid != -1)
                {
                    if (livant[tmpantid].getDirection() == false)
                    {
                        if (abs (livant[i].getLocation () - livant[tmpantid].getLocation ()) <= (livant[i].getVelocity () + livant[tmpantid].getVelocity ()))
                        {
                            livant[i].changeDirection ();
                            livant[tmpantid].changeDirection ();
                        }
                    }
                    else
                    {
                        if (abs (livant[i].getLocation () - livant[tmpantid].getLocation ()) <= abs (livant[i].getVelocity () - livant[tmpantid].getVelocity ()))
                        {
                            livant[i].changeDirection ();
                            livant[tmpantid].changeDirection ();
                        }
                    }
                }
            }
        }
    }
}

/*
 Post: The function judges whether all the ants are dead.
 */
bool CreepingGame::isAllDead ()
{
    if (curstick.getLivAntCnt () <= 0)
        return true;
    return false;
}

/*
 Post: The status of the game is returned.
 */
bool CreepingGame::getGameStatus ()
{
    return isGameOver;
}

/*
 Post: The time of the game is returned.
 */
int CreepingGame::getGameTime ()
{
    return gameTime;
}


/*
 The GameRoom class.
 */
class GameRoom
{
private:
    int incTime;
    GameData stGameData[DefaultAnt.GroupCnt];
    
public:
    GameRoom ();
    
    void buildDirections ();
    void playGames (double &maxTime, double &minTime);
    void start (double &maxTime, double &minTime);
    
};

/*
 Post: The function initializes the class, with the time of loop initialized by 1 second.
 */
GameRoom::GameRoom ()
{
    incTime = 1;
    
    for (int i = 0; i < DefaultAnt.GroupCnt; i ++)
        stGameData[i].gameTime = 0;
}

/*
 Post: Every situation is generalized.
 */
void GameRoom::buildDirections ()
{
    for (int divis = 0; divis < DefaultAnt.antcnt; divis ++)
    {
        int tmpdivi = pow (2, divis);
        for (int i = 0; i < DefaultAnt.GroupCnt; i += 2 * tmpdivi)
        {
            for (int j = i; j < tmpdivi; j ++)
                stGameData[j].antDirection[divis] = true;
            for (int p = i + tmpdivi; p < i + 2 * tmpdivi; p ++)
                stGameData[p].antDirection[divis] = false;
        }
    }
}

/*
 Post: Games are played. maxTime is given the value of the maximum time while minTime is given the value of the minimum time.
 */
void GameRoom::playGames (double &maxTime, double &minTime)
{
    CreepingGame creep[DefaultAnt.GroupCnt];
    for (int i = 0; i < DefaultAnt.GroupCnt; i ++)
    {
        creep[i].startGame (stGameData[i]);
        creep[i].drivingGame();
        stGameData[i].gameTime = creep[i].getGameTime ();
    }
    
    int maxtime = 0;
    int mintime = stGameData[0].gameTime;
    
    for (int j = 0; j < DefaultAnt.GroupCnt; j ++)
    {
        if (stGameData[j].gameTime > maxtime)
            maxtime = stGameData[j].gameTime;
        if (stGameData[j].gameTime < mintime)
            mintime = stGameData[j].gameTime;
    }
    
    maxTime = maxtime;
    minTime = mintime;
    
}

/*
 Post: Game starts.
 */
void GameRoom::start (double &maxTime, double &minTime)
{
    std::cout << "Game started." << std::endl;
    buildDirections ();
    playGames (maxTime, minTime);
}

#endif /* Ant_h */
