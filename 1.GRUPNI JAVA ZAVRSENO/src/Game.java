/* Grupa 11
   Clanovi:
   Aleksa Stupar 24/105
   Dalil Hadziahmetovic 24/043 
 */

class Player {

    private String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private int health;

    public Player(String name, int x, int y, int width, int height, int health) {
        setName(name);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setHealth(health);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        name = name.trim();

        while (name.contains("  ")) {
            name = name.replace("  ", " ");
        }

        if (name.length() > 0) {

            String[] rijeci = name.split(" ");
            String novoIme = "";

            for (int i = 0; i < rijeci.length; i++) {

                novoIme = novoIme
                        + rijeci[i].substring(0, 1).toUpperCase()
                        + rijeci[i].substring(1).toLowerCase();

                if (i < rijeci.length - 1) {
                    novoIme = novoIme + " ";
                }
            }

            this.name = novoIme;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {

        if (health < 0) {
            this.health = 0;
        }
        else if (health > 100) {
            this.health = 100;
        }
        else {
            this.health = health;
        }
    }

    public String toString() {

        return "Player[" + name + "] @ ("
                + x + "," + y + ") "
                + width + "x" + height
                + " HP=" + health;
    }
}


class Enemy {

    private String type;
    private int x;
    private int y;
    private int width;
    private int height;
    private int damage;

    public Enemy(String type, int x, int y, int width, int height, int damage) {
        setType(type);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setDamage(damage);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {

        type = type.trim();

        if (type.length() == 0) {
            this.type = "Nepoznat";
        }
        else {
            this.type = type;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {

        if (damage < 0) {
            this.damage = 0;
        }
        else if (damage > 100) {
            this.damage = 100;
        }
        else {
            this.damage = damage;
        }
    }

    public String toString() {

        return "Enemy[" + type + "] @ ("
                + x + "," + y + ") "
                + width + "x" + height
                + " DMG=" + damage;
    }
}


public class Game {

    private Player player;
    private Enemy[] enemies;
    private String eventLog;

    public static void main(String[] args) {

        Player p = new Player("marko   markovic", 10, 5, 32, 32, 85);

        Game game = new Game(p);

        Enemy e1 = new Enemy("Goblin", 50, 20, 16, 16, 15);
        game.addEnemy(e1);

        String tekst = "Goblin;12,5;16x16;20";

        Enemy e2 = napraviEnemy(tekst);
        game.addEnemy(e2);

        System.out.println("Svi neprijatelji:");

        for (int i = 0; i < game.enemies.length; i++) {
            System.out.println(game.enemies[i]);
        }

        System.out.println("Pretraga za gob:");

        Enemy[] pronadjeni = game.findByType("gob");

        for (int i = 0; i < pronadjeni.length; i++) {
            System.out.println(pronadjeni[i]);
        }

        System.out.println("Igrac prije sudara:");
        System.out.println(p);

        game.resolveCollisions();

        System.out.println("Igrac poslije sudara:");
        System.out.println(p);

        System.out.println("Event log:");
        System.out.println(game.eventLog);
    }

    public Game(Player player) {
        this.player = player;
        this.enemies = new Enemy[0];
        this.eventLog = "";
    }

    public void addEnemy(Enemy e) {

        Enemy[] noviNiz = new Enemy[enemies.length + 1];

        for (int i = 0; i < enemies.length; i++) {
            noviNiz[i] = enemies[i];
        }

        noviNiz[enemies.length] = e;
        enemies = noviNiz;

        eventLog = eventLog + "Dodat neprijatelj: " + e.getType() + "\n";
    }

    public boolean checkCollision(Player p, Enemy e) {

        if (p.getX() < e.getX() + e.getWidth()
                && p.getX() + p.getWidth() > e.getX()
                && p.getY() < e.getY() + e.getHeight()
                && p.getY() + p.getHeight() > e.getY()) {

            return true;
        }

        return false;
    }

    public void decreaseHealth(Player p, Enemy e) {

        int stariHealth = p.getHealth();

        int noviHealth = stariHealth - e.getDamage();

        if (noviHealth < 0) {
            noviHealth = 0;
        }

        p.setHealth(noviHealth);

        eventLog = eventLog + "Igrac je udaren od "
                + e.getType()
                + ", damage: " + e.getDamage()
                + ", health: " + stariHealth
                + " -> " + noviHealth + "\n";
    }

    public Enemy[] findByType(String query) {

        int brojac = 0;

        query = query.toLowerCase();

        for (int i = 0; i < enemies.length; i++) {

            String tip = enemies[i].getType().toLowerCase();

            if (tip.contains(query)) {
                brojac++;
            }
        }

        Enemy[] pronadjeni = new Enemy[brojac];

        int j = 0;

        for (int i = 0; i < enemies.length; i++) {

            String tip = enemies[i].getType().toLowerCase();

            if (tip.contains(query)) {
                pronadjeni[j] = enemies[i];
                j++;
            }
        }

        return pronadjeni;
    }

    public Enemy[] collidingWithPlayer() {

        int brojac = 0;

        for (int i = 0; i < enemies.length; i++) {

            if (checkCollision(player, enemies[i])) {
                brojac++;
            }
        }

        Enemy[] sudari = new Enemy[brojac];

        int j = 0;

        for (int i = 0; i < enemies.length; i++) {

            if (checkCollision(player, enemies[i])) {
                sudari[j] = enemies[i];
                j++;
            }
        }

        return sudari;
    }

    public void resolveCollisions() {

        Enemy[] sudari = collidingWithPlayer();

        for (int i = 0; i < sudari.length; i++) {
            decreaseHealth(player, sudari[i]);
        }
    }

    public static Enemy napraviEnemy(String tekst) {

        String[] djelovi = tekst.split(";");

        String type = djelovi[0];

        String[] pozicija = djelovi[1].split(",");

        int x = Integer.parseInt(pozicija[0]);
        int y = Integer.parseInt(pozicija[1]);

        String[] velicina = djelovi[2].split("x");

        int width = Integer.parseInt(velicina[0]);
        int height = Integer.parseInt(velicina[1]);

        int damage = Integer.parseInt(djelovi[3]);

        Enemy e = new Enemy(type, x, y, width, height, damage);

        return e;
    }
}    