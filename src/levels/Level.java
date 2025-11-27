package levels;

import main.Game;
import entities.Crabby;
import objects.*;
import utilz.HelpMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.HelpMethods.*;

public class Level {

    private BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<WaterBottom> waterBottoms;
    private ArrayList<WaterTop> waterTops;
    private ArrayList<GameContainer> containers;
    private ArrayList<Cannon> cannons;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;
    private Point playerSpawn;
    private Point magicBeanSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpike();
        createWaterTop();
        createWaterBottom();
        createCannons();
        calcLvlOffsets();
        calcMagicBeanSpawn();
        calcPlayerSpawn();
    }

    private void createCannons() {
        cannons = HelpMethods.GetCannons(img);
    }

    private void createSpike() {
        spikes = HelpMethods.GetSpike(img);
    }

    private void createWaterTop() {
        waterTops = HelpMethods.GetWaterTop(img);
    }

    private void createWaterBottom() {
        waterBottoms = HelpMethods.GetWaterBottom(img);

    }

    private void createContainers() {
        containers = HelpMethods.GetContainers(img);
    }

    private void createPotions() {
        potions = HelpMethods.GetPotions(img);
    }

    private void calcPlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);
    }

    private void calcMagicBeanSpawn() {
        magicBeanSpawn = GetMagicBeanSpawn(img);
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
        maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        crabs = Getcrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }

    public Point getMagicBeanSpawn() {
        return magicBeanSpawn;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getContainers() {
        return containers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    public ArrayList<WaterTop> getWaterTops() {
        return waterTops;
    }

    public ArrayList<WaterBottom> getWaterBottoms() {
        return waterBottoms;
    }
}
