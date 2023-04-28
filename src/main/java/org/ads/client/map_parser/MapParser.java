package org.ads.client.map_parser;

import org.ads.client.GamePanel;
import org.ads.client.UtilityTool;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;

public class MapParser {
    GamePanel gamePanel;
    public String mapPath;
    public int width;
    public int height;
    public ArrayList<int[][]> layers;
    public Tile[] tiles;
    public int tileHeight;

    public MapParser(String mapPath, GamePanel gamePanel) throws IOException {
        this.gamePanel = gamePanel;
        this.mapPath = mapPath;
        String inputStreamReader = new String(Objects.requireNonNull(getClass().getResourceAsStream(mapPath)).readAllBytes(), StandardCharsets.UTF_8);
        JSONObject map = new JSONObject(inputStreamReader);

        width = map.getInt("width");
        height = map.getInt("height");
        tileHeight = map.getInt("tileheight");

    }

    public ArrayList<Integer> getMapInfo(){
        ArrayList<Integer> result = new ArrayList<>();
        result.add(width);
        result.add(height);
        result.add(tileHeight);
        return result;
    }

    public void loadMap() throws IOException {
        String inputStreamReader = new String(Objects.requireNonNull(getClass().getResourceAsStream(mapPath)).readAllBytes(), StandardCharsets.UTF_8);
        JSONObject map = new JSONObject(inputStreamReader);

        layers = new ArrayList<>();
        JSONArray layersArray = map.getJSONArray("layers");
        for (int i = 0; i < layersArray.length(); i++) {
            JSONArray mapJsonArray = layersArray.getJSONObject(i).getJSONArray("data");
            int[][] currentLayer = new int[width][height];

            int k = 0;
            for (int l = 0; l < width; l++) {
                for (int j = 0; j < height; j++) {
                    currentLayer[j][l] = mapJsonArray.getInt(k);
                    k++;
                }
            }
            layers.add(currentLayer);
        }

        // Load tileset
        inputStreamReader = new String(Objects.requireNonNull(getClass().getResourceAsStream("/tilesets/tileset.json")).readAllBytes(), StandardCharsets.UTF_8);
        JSONObject tileset = new JSONObject(inputStreamReader);

        tiles = new Tile[tileset.getInt("tilecount")+1];

        JSONArray tileJsonArray = tileset.getJSONArray("tiles");

        for (int i = 0; i < tileJsonArray.length(); i++) {
            JSONObject object = tileJsonArray.getJSONObject(i);

            int index = object.getInt("id")+1;
            String imagePath = object.getString("image");
            imagePath = imagePath.substring(imagePath.lastIndexOf("/")+1);
            Boolean hasCollision = object.getJSONArray("properties").getJSONObject(0).getBoolean("value");

            setupTile(index, imagePath, hasCollision);
        }
    }
    private void setupTile(int index, String imagePath, Boolean hasCollision) throws IOException {
        UtilityTool utilityTool = new UtilityTool();

        tiles[index] = new Tile();
        tiles[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/"+imagePath)));
        tiles[index].image = utilityTool.scaleImage(tiles[index].image, gamePanel.tileSize, gamePanel.tileSize);
        tiles[index].collision = hasCollision;
    }

    public void draw(Graphics2D graph2D) {
        for (int[][] layer : layers) {
            int worldCol = 0;
            int worldRow = 0;

            while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {

                int tileNumber = layer[worldCol][worldRow];

                int worldX = worldCol * gamePanel.tileSize;
                int worldY = worldRow * gamePanel.tileSize;
                int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
                int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

                // Stop moving camera at the edge of map
                if (gamePanel.player.screenX > gamePanel.player.worldX) {
                    screenX = worldX;
                }
                if (gamePanel.player.screenY > gamePanel.player.worldY) {
                    screenY = worldY;
                }
                int rightOffset = gamePanel.screenWidth - gamePanel.player.screenX;
                if (rightOffset > gamePanel.worldWidth - gamePanel.player.worldX) {
                    screenX = gamePanel.screenWidth - (gamePanel.worldWidth - worldX);
                }
                int bottomOffset = gamePanel.screenHeight - gamePanel.player.screenY;
                if (bottomOffset > gamePanel.worldHeight - gamePanel.player.worldY) {
                    screenY = gamePanel.screenHeight - (gamePanel.worldWidth - worldY);
                }


                if ((worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
                        worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
                        worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
                        worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) ||
                        gamePanel.player.screenX > gamePanel.player.worldX ||
                        gamePanel.player.screenY > gamePanel.player.worldY ||
                        rightOffset > gamePanel.worldWidth - gamePanel.player.worldX ||
                        bottomOffset > gamePanel.worldHeight - gamePanel.player.worldY) {
                    if (tileNumber != 0) {
                        graph2D.drawImage(tiles[tileNumber].image, screenX, screenY, null);
                    }
                }

                worldCol++;

                if (worldCol == gamePanel.maxWorldCol) {
                    worldCol = 0;
                    worldRow++;
                }
            }
        }
    }
}
