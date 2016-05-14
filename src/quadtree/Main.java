package quadtree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static Quadtree t = null;
    private static final BufferedReader BR = new BufferedReader(new InputStreamReader(System.in));
    private static ArrayList<Point> candidates;
    private static boolean finished;

    public static void main(String[] args) throws IOException {
        if (args[0].equals("i")) {
            System.out.println("Mode interactif.");
            String s;
            if (args.length == 2 && args[1].equals("t")) {
                generateTree(100, 50);
            }
            finished = false;
            do {
                System.out.print("Tapez une commande (h ou help pour l'aide) >");
                s = BR.readLine();
                treatCommand(s);
            } while (!finished);
            System.exit(0);
        } else {
            if (args[0].equals("a")) {
                generateTree(100, 50);
                for(Point p: candidates){
                    printPointDeepness(p.getX(), p.getY());
                    printClosests(p.getX(), p.getY());
                }
            }
        }
    }

    private static void treatCommand(String s) throws IOException {
        switch (s) {
            case "help":
            case "h":
                System.out.println(help);
                break;
            case "q":
            case "quit":
                System.out.println("@+");
                finished = true;
                break;
            case "afficher":
            case "a":
                if (t == null) {
                    System.out.println("Veuillez commencer par générer un arbre à l'aide de la commande t.");
                } else {
                    t.printInConsole();
                }
                break;
            case "tree":
            case "t":
                int size = 100;
                int nb = 50;
                generateTree(size, nb);
                break;
            case "profondeur":
            case "pr":
                if (t == null) {
                    System.out.println("Veuillez commencer par générer un arbre à l'aide de la commande t.");
                } else {
                    int x = getIntFromUser("Donnez l'abscisse du point");
                    int y = getIntFromUser("Donnez l'ordonnée du point");
                    printPointDeepness(x, y);
                }
                break;
            case "points":
            case "p":
                if (t == null) {
                    System.out.println("Veuillez commencer par générer un arbre à l'aide de la commande t.");
                } else {
                    printCandidates();
                }
                break;
            case "voisins":
            case "v":
                if (t == null) {
                    System.out.println("Veuillez commencer par générer un arbre à l'aide de la commande t.");
                } else {
                    int x = getIntFromUser("Donnez l'abscisse du point");
                    int y = getIntFromUser("Donnez l'ordonnée du point");
                    printClosests(x, y);
                }
                break;
            default:
                System.out.println(help);
        }
    }
    
    private static final String help = "Manuel d'utilisation : \n"
            + "h ou help : Afficher ce message d'aide\n"
            + "t ou tree : Générer un nouveau Quadtree\n"
            + "a ou afficher : Afficher le Quadtree\n"
            + "p ou points : Afficher le Quadtree\n"
            + "pr ou profondeur : trouver la profondeur d'un point\n"
            + "v ou voisins : trouver les voisins proches d'un point\n"
            + "q ou quit : trouver les voisins proches d'un point\n";

    private static int getIntFromUser(String ask) throws IOException {
        System.out.print(ask + " >");
        String s = BR.readLine();
        int size = -1;
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(s);
        if (m.matches()) {
            size = Integer.parseInt(s);
        } else {
            System.out.println("Erreur de format, entrez une valeur entière.");
        }
        return size;
    }

    private static void printCandidates() {
        System.out.println("Voici les coordonnées de 10 points de la grille.");
        for (Point p : candidates) {
            System.out.println("\t(" + p.getX() + "," + p.getY() + ")");
        }
    }

    private static void generateTree(int size, int nb) {
        if (size != -1 && nb != -1) {
            t = new Quadtree(size, nb);
            System.out.println("Nouveau Quadtree généré, profondeur maximale : " + t.getMaxdeep());
            candidates = t.getRandomPoints(10);
//                    t.draw();
        }
    }

    private static void printPointDeepness(int x, int y) {
        int p = t.getDeepness(x, y);
        if (p < 0 && p > -9999) {
            System.out.println("Attention le point (" + x + "," + y + ") ne fait pas partie du QuadTree.");
            p = -p;
        }
        if (p == -9999) {
            System.out.println("Attention le point (" + x + "," + y + ") ne fait pas partie de la grille.");
        } else {
            System.out.println("Le point (" + x + "," + y + ") à une profondeur de " + p);
        }
    }

    private static void printClosests(int x, int y) {
        ArrayList<Point> vs = t.getClosests(x, y);
        System.out.println("Le point (" + x + "," + y + ") possède " + vs.size() + " voisins proches :");
        for (Point p : vs) {
            System.out.println("\t(" + p.getX() + "," + p.getY() + ")");
        }
    }
}
