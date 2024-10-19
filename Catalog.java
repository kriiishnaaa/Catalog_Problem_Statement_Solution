import org.json.JSONObject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Catalog {

    public static void main(String[] args) {
        // Sample JSON input
        String jsonString = "{\n" +
                "\"keys\": {\"n\": 4, \"k\": 3},\n" +
                "\"1\": {\"base\": \"10\", \"value\": \"4\"},\n" +
                "\"2\": {\"base\": \"2\", \"value\": \"111\"},\n" +
                "\"3\": {\"base\": \"10\", \"value\": \"12\"},\n" +
                "\"6\": {\"base\": \"4\", \"value\": \"213\"}\n" +
                "}";

        // Parse the JSON input
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject keys = jsonObject.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        // Store points (x, y) for Lagrange interpolation
        Map<Integer, BigInteger> points = new HashMap<>();

        // Parse the roots from the JSON and decode the y values
        for (String key : jsonObject.keySet()) {
            if (key.equals("keys")) continue;

            JSONObject root = jsonObject.getJSONObject(key);
            int base = root.getInt("base");
            String value = root.getString("value");
            BigInteger y = new BigInteger(value, base);
            int x = Integer.parseInt(key);

            // Store the (x, y) point
            points.put(x, y);
        }

        // Perform Lagrange Interpolation to find the constant term c
        BigInteger secret = lagrangeInterpolation(points, k);
        System.out.println("The secret (constant term c) is: " + secret);
    }

    // Function for Lagrange interpolation
    public static BigInteger lagrangeInterpolation(Map<Integer, BigInteger> points, int k) {
        BigInteger constantTerm = BigInteger.ZERO;

        for (Map.Entry<Integer, BigInteger> i : points.entrySet()) {
            int xi = i.getKey();
            BigInteger yi = i.getValue();

            // Lagrange basis polynomial
            BigInteger term = yi;
            for (Map.Entry<Integer, BigInteger> j : points.entrySet()) {
                int xj = j.getKey();
                if (xi != xj) {
                    term = term.multiply(BigInteger.valueOf(-xj))
                            .divide(BigInteger.valueOf(xi - xj));
                }
            }
            constantTerm = constantTerm.add(term);
        }

        return constantTerm;
    }
}
