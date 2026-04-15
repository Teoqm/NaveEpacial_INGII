package autonoma.nave_epacial.Io;

import autonoma.nave_epacial.gameObjects.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Gestiona la persistencia de puntuaciones en formato JSON.
 * @version 1.0
 */
public class JSONParser {

    /**
     * Lee el archivo de puntuaciones y retorna la lista de registros.
     *
     * @return lista de {@link ScoreData} leídos del archivo.
     * @throws FileNotFoundException si el archivo no puede accederse.
     */
    public static ArrayList<ScoreData> readFile() throws FileNotFoundException {
        ArrayList<ScoreData> dataList = new ArrayList<>();
        File file = new File(Constants.SCORE_PATH);

        if (!file.exists() || file.length() == 0) return dataList;

        JSONTokener parser  = new JSONTokener(new FileInputStream(file));
        JSONArray   jsonList = new JSONArray(parser);

        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject obj  = jsonList.getJSONObject(i);
            ScoreData  data = new ScoreData();
            data.setScore(obj.getInt("score"));
            data.setDate(obj.getString("date"));
            // Compatibilidad con registros antiguos sin campo team
            data.setTeam(obj.optString("team", "Equipo"));
            dataList.add(data);
        }

        return dataList;
    }

    /**
     * Serializa la lista de puntuaciones a JSON y la guarda en disco.
     *
     * @param dataList lista de {@link ScoreData} a persistir.
     * @throws IOException si ocurre un error de escritura.
     */
    public static void writeFile(ArrayList<ScoreData> dataList) throws IOException {
        System.out.println("[JSON] Guardando en: " + new java.io.File(Constants.SCORE_PATH).getAbsolutePath());
        File outputFile = new File(Constants.SCORE_PATH);
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        JSONArray jsonList = new JSONArray();
        for (ScoreData data : dataList) {
            JSONObject obj = new JSONObject();
            obj.put("team",  data.getTeam());
            obj.put("score", data.getScore());
            obj.put("date",  data.getDate());
            jsonList.put(obj);
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile.toURI()));
        jsonList.write(writer);
        writer.close();
    }
}