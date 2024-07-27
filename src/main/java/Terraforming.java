

import java.io.*;
import java.util.*;

class Terraforming {
    static class Node {
        int x, y;
        int biomeIndex;
        double quality;
        
        Node(int x, int y, int biomeIndex, double quality) {
            this.x = x;
            this.y = y;
            this.biomeIndex = biomeIndex;
            this.quality = quality;
        }
    }

    static final int[] dx = {1, -1, 0, 0};
    static final int[] dy = {0, 0, 1, -1};
    static final int MAX_DAYS = 37;  // Adjust based on level
    static final int MAX_MOVES = 3;

    static Map<Integer, Integer> biomePoints = Map.of(
        0, 1,
        1, 14,
        2, 28,
        3, 42,
        4, 57,
        5, 71,
        6, 85,
        7, 100
    );

    public static void main(String[] args) {
        String filename = "planet.txt"; // Replace with your file name
        List<Node> nodes = readNodesFromFile(filename);
        List<int[]> path = findOptimalPath(nodes);
        int score = calculateScore(nodes, path);
        
        System.out.println("Optimal Path: " + path);
        System.out.println("Score: " + score);
    }

    static List<Node> readNodesFromFile(String filename) {
        List<Node> nodes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("{") && line.endsWith("}")) {
                    line = line.substring(1, line.length() - 1);
                    String[] parts = line.split(";");
                    String[] coords = parts[0].replace("(", "").replace(")", "").split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    if (parts.length == 3) {
                        int biomeIndex = Integer.parseInt(parts[1]);
                        double quality = Double.parseDouble(parts[2]);
                        nodes.add(new Node(x, y, biomeIndex, quality));
                    } else {
                        nodes.add(new Node(x, y, -1, -1));  // North or South Pole
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    static List<int[]> findOptimalPath(List<Node> nodes) {
        // Simplified version using a straight path
        List<int[]> path = new ArrayList<>();
        path.add(new int[]{0, 11});
        for (int i = 10; i > -11; i--) {
            path.add(new int[]{0, i});
        }
        path.add(new int[]{0, -11});
        return path;
    }

    static int calculateScore(List<Node> nodes, List<int[]> path) {
        Map<String, Node> nodeMap = new HashMap<>();
        for (Node node : nodes) {
            nodeMap.put(node.x + "," + node.y, node);
        }
        
        int score = 0;
        int days = MAX_DAYS;
        for (int i = 0; i < path.size(); i++) {
            int[] coord = path.get(i);
            Node node = nodeMap.get(coord[0] + "," + coord[1]);
            if (node != null && node.biomeIndex != -1) {
                int points = biomePoints.get(node.biomeIndex);
                double quality = node.quality;
                score += points * quality * (days - i);
            }
        }
        return score;
    }
}

	

