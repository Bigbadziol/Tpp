/*
TODO : Ladny zapis do pliku , usunac nadmiar komentarzy do konsoli (testowe)
Dp pliku pom.xml

Do sekcji - gdyby nie zassalo Gsona
<dependencies>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.8.6</version>
        </dependency>
<dependencies>

Do sekcji <plugins> DODAJ TO :
W sekcji <outputDirectory> - ustaw sciezke dostepu do katalogu plugins
-Bardzo wazne , przeladuj mavena
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-jar-plugin</artifactId>
        <version>2.3.1</version>
            <configuration>
                <outputDirectory>D:\BuildToolSpigot\plugins</outputDirectory>
            </configuration>
    </plugin>

 */

package badziol.tpp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;

public final class Tpp extends JavaPlugin implements Listener {
    ListaPunktowTeleportacji mojePunkty = new ListaPunktowTeleportacji();
    //sposob 1) na odwolanie statyczne do instaancji pluginu.
    //inne klasy muszą wiedzieć jak współdziałać
    //Tpp(jest tu typem danych bo to klasa) plugin to tak naprawde nazwa naszej zmiennej
    private static Tpp plugin;

    /**
     * Metoda to przekazywania instancji pluginu
     * @return Zwraca instancję pluginu
     */
    public static Tpp getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("[TPplugin] - uruchamiam... :1.12, wywalony listener, czysty rtp");
        //NIE KASOWAC 2 linii ponizej,  MOZE sie przydac do Windows/Linux różnic w podejsciu do sciezek
        //String test = Tpp.getPlugin().getDataFolder().getAbsolutePath() + "/punkty.json";
        //System.out.printf("test sciezki : %s \n",test);
        //mojePunkty.bazowePunkty(); //wylaczamy nasze zdefiniowane wczesniej punkty , wszystko teraz jest
                                     //przechowywane w pliku zewnetrznym
        try {
            mojePunkty.wczytajPlik();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //dodanie dodatkowej komendy dla pluginu opisanej w innej klasie
        //metoda ponoc zalecana
        //orginalne  moje // getCommand("xxx").setExecutor(new komendaXXX());
        //zalecane
        Objects.requireNonNull(getCommand("xxx")).setExecutor(new komendaXXX());
    }

    @Override
    public void onDisable() {
        System.out.println("[TPplugin] - zamykam");
    }

    /**
     * Przeslicznie  pokolorowany opis funkcjonalności pluginu
     * @param gracz - obiekt gracza , ktoremu ma myc wyswietlony opis
     */
    void opisDzialania(Player gracz){
        gracz.sendMessage(ChatColor.GOLD+"Opis dzialania :");
        gracz.sendMessage(ChatColor.BLUE+"lista"+ChatColor.WHITE+" - lista utworzonych punktow.");
        gracz.sendMessage(ChatColor.BLUE+"dodaj"+ChatColor.DARK_AQUA+" [nazwa]"+ChatColor.WHITE+" - unikalna , skrypt zapamieta wspolrzedne gdzie stoisz.");
        gracz.sendMessage(ChatColor.BLUE+"usun"+ChatColor.DARK_AQUA+ "[nazwa]"+ChatColor.WHITE+ "- usuniecie wskazanego punktu");
        gracz.sendMessage(ChatColor.BLUE+"tp"+ChatColor.WHITE+" - przeniesienie do losowego punktu z listy.");
        gracz.sendMessage(ChatColor.BLUE+"idz"+ChatColor.DARK_AQUA+" [nazwa]"+ChatColor.WHITE+" - przeniesienie do wskazanego punktu.");
        gracz.sendMessage(ChatColor.DARK_PURPLE+"Sprawdz tez komende xxx lub jej aliasy yyy , zzz");
    }

    /**
     *  Na podstawie aktualnej pozycji gracza X,Y,Z oraz kierunku jego wzroku Yaw, Pitch
     *  tworzony jest nowy punkt
     * @param gracz - obiekt gracza wzgledem ktorego  pozycji tworzony jest punkt
     * @param nazwaPunktu - unikalna nazwa punktu
     */
    void dodajPunkt(Player gracz,String nazwaPunktu){
        double x = gracz.getLocation().getX();
        double y = gracz.getLocation().getY();
        double z = gracz.getLocation().getZ();
        float pitch = gracz.getLocation().getPitch();
        float yaw = gracz.getLocation().getYaw();
        String swiat = gracz.getWorld().getName();
        PunktTeleportacji a = new PunktTeleportacji(nazwaPunktu,swiat, x, y, z, pitch, yaw);
        mojePunkty.dodaj(a);
        try {
            mojePunkty.zapiszPlik();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    String usunPunkt(String nazwaPunktu){
        String wynikUsuniecia = mojePunkty.usun(nazwaPunktu);
        try {
            mojePunkty.zapiszPlik();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wynikUsuniecia;
    }

    /**
     * Metoda wykozystywana przez podkomendy "idz" oraz "tp - przenosi gracza do wskazanego miejsca
     * @param gracz - teleportowany gracz
     * @param docelowyPunkt - docelowy punkt przeniesioenia
     * @return true - jesli operacja zakonczona sukcesem
     */
    Boolean przeniesGracza(Player gracz , PunktTeleportacji docelowyPunkt){
        Location lokacja = new Location(Bukkit.getWorld(docelowyPunkt.getSwiat()),
                docelowyPunkt.getX(),docelowyPunkt.getY(),docelowyPunkt.getZ());
        lokacja.setYaw(docelowyPunkt.getYaw());
        lokacja.setPitch(docelowyPunkt.getPitch());
        return gracz.teleport(lokacja);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rtp")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                // zawsze nazwaPluginu.nazwaPermission
                if (!p.hasPermission("rtp.rtp")){
                    p.sendMessage("Nie masz uprawnień do posługiwania się tym pluginem");
                    return true;
                }
                // komenda wywolana bez parametrow , wypisz informacje jak dziala plugin
                if (args.length == 0){
                    opisDzialania(p);
                    return true;
                }
                //jesli komenda wywolana z parametrem "dodaj","usun","idz" wymagana jest nastepnie nazwa
                //punktu ktorego się on dotyczy , brak nazwy - oznacza blad
                if ( (args[0].equalsIgnoreCase("dodaj") ||
                        args[0].equalsIgnoreCase("usun") ||
                        args[0].equalsIgnoreCase("idz"))  && args.length == 1){
                        String info;
                         info="(TPplugin) -";
                         info += args[0].toUpperCase() + " - ";
                         info += "nie podano nazwy punktu.";
                         p.sendMessage(info);
                    return true;
                }
                //LISTA PUNKTOW
                if(args[0].equalsIgnoreCase("lista")){
                    System.out.println("(TPplugin) Wywolano [lista]");
                    p.sendMessage(mojePunkty.wypiszListe()); // informacja dla gracza
                    System.out.println(mojePunkty.wypiszListe());// informacja na konsole
                    return true;
                }
                //DODANIE NOWEGO PUNKTU
                else if (args[0].equalsIgnoreCase("dodaj")) {
                    System.out.println("(TPplugin) Wywolano [dodaj]");
                    dodajPunkt(p,args[1]);
                    return true;
                }
                //USUWANIE PUNKTU
                else if(args[0].equalsIgnoreCase("usun")){
                    System.out.println("(TPplugin) Wywolano [usun]");
                    String nazwaPunktu = args[1];
                    String wynikUsuniecia = usunPunkt(nazwaPunktu);
                    p.sendMessage(wynikUsuniecia);
                    System.out.println(wynikUsuniecia); // informacja na konsole
                    return true;
                }
                //IDZ DO WSKAZANEGO PUNKTU
                else if(args[0].equalsIgnoreCase("idz")){
                    System.out.println("(TPplugin) Wywolano [tp]");
                    PunktTeleportacji punkt = mojePunkty.wezPunkt(args[1]);
                    if (punkt.getNazwa().isEmpty()) { //pusta nazwa to blad
                        p.sendMessage("Bledna nazwa punktu docelowego.");
                    }else {
                        Boolean wynikTelportacji = przeniesGracza(p, punkt);
                        if (wynikTelportacji) {
                            p.sendMessage("Jestes w punkcie :"  +ChatColor.YELLOW+ punkt.getNazwa());
                        }else{
                            p.sendMessage(ChatColor.RED+"Ups...nie udalo sie przeniesc gracza.");
                        }
                    }
                    return true;
                }
                //TELEPORTACJA DO LOSOWEGO PUNKTU
                else if (args[0].equalsIgnoreCase("tp")) {
                    System.out.println("(TPplugin) Wywolano [tp]");
                    PunktTeleportacji wylosowanyPunkt = mojePunkty.losujPunkt();
                    Boolean wynikTeleportacji = przeniesGracza(p,wylosowanyPunkt);
                    if (wynikTeleportacji){
                        p.sendMessage("Jestes w punkcie :" +ChatColor.GOLD+wylosowanyPunkt.getNazwa());
                    }else{
                        p.sendMessage(ChatColor.RED+"Ups...coś poszlo nie tak.");
                    }
                    return true;
                }
                //NIEZNANY PARAMETR
                else{
                    p.sendMessage(ChatColor.DARK_RED+"Nieznany parametr.");
                }
            //OBSLUGA KONSOLI
            }else if (sender instanceof ConsoleCommandSender){
                System.out.println("Nie wazne jakie dasz parametry i tak wypisze tylko liste.");
                System.out.println("Tylko lista punktow jest zaimplementowana dla konsoli.");
                System.out.println(mojePunkty.wypiszListe());// informacja na konsole
            }
        }//rtp - obsluga komendy
        return true;
    }
}