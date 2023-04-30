package org.ads.server.map_parser;

import org.ads.server.GameThread;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class MapParser {
    GameThread gameThread;
    public String mapPath;
    public int width;
    public int height;
    public ArrayList<int[][]> layers;
    public Tile[] tiles;
    public int tileHeight;

    public MapParser(String mapPath, GameThread gameThread) throws IOException {
        this.gameThread = gameThread;
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
            Boolean hasCollision = object.getJSONArray("properties").getJSONObject(0).getBoolean("value");

            setupTile(index, hasCollision);
        }
    }
    private void setupTile(int index, Boolean hasCollision) {

        tiles[index] = new Tile();
        tiles[index].collision = hasCollision;
    }
}
