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
 * La clase JSONParser se encarga de la gestión de persistencia de datos para el juego.
 * Proporciona métodos estáticos para leer y escribir archivos en formato JSON,
 * permitiendo almacenar el historial de puntuaciones (High Scores) de manera local.
 * * @version 1.0
 */
public class JSONParser {

    /**
     * Lee el archivo de puntuaciones definido en las constantes del juego y
     * transforma el contenido JSON en una lista de objetos ScoreData.
     * * @return Una lista {@link ArrayList} de {@link ScoreData} con los registros encontrados.
     * Si el archivo no existe o está vacío, devuelve una lista vacía.
     * @throws FileNotFoundException Si ocurre un error al intentar acceder al archivo físico.
     */
    public static ArrayList<ScoreData> readFile() throws FileNotFoundException {
        ArrayList<ScoreData> dataList = new ArrayList<>();

        File file = new File(Constants.SCORE_PATH);

        if (!file.exists() || file.length() == 0){
            return dataList;
        }

        JSONTokener parser = new JSONTokener(new FileInputStream(file));
        JSONArray jsonList = new JSONArray(parser);

        for (int i = 0; i < jsonList.length(); i++) {
            JSONObject obj = jsonList.getJSONObject(i);
            ScoreData data = new ScoreData();
            data.setScore(obj.getInt("score"));
            data.setDate(obj.getString("date"));
            dataList.add(data);
        }

        return dataList;
    }

    /**
     * Serializa una lista de objetos ScoreData a formato JSON y la guarda en el disco duro.
     * Si las carpetas del destino no existen, el método intenta crearlas automáticamente.
     * * @param dataList La lista de objetos {@link ScoreData} que se desea persistir.
     * @throws IOException Si ocurre un error durante la creación del archivo o el proceso de escritura.
     */
    public static void writeFile (ArrayList<ScoreData> dataList) throws IOException {
        File outputFile = new File(Constants.SCORE_PATH);
        outputFile.getParentFile().mkdirs();
        outputFile.createNewFile();

        JSONArray jsonList = new JSONArray();
        for (ScoreData data : dataList) {
            JSONObject obj = new JSONObject();
            obj.put("score", data.getScore());
            obj.put("date", data.getDate());

            jsonList.put(obj);
        }

        BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile.toURI()));
        jsonList.write(writer);
        writer.close();
    }
}