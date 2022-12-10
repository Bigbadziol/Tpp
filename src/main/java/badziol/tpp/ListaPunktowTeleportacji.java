package badziol.tpp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ListaPunktowTeleportacji {
    private final ArrayList<PunktTeleportacji> lista = new ArrayList<PunktTeleportacji>();

    /**
     *  Zbuduj stringa przypominajacego tabelę zawierającą wszystki zdefiniowane punkty
     * @return Tabela jako string
     */
    String  wypiszListe(){
        StringBuilder ret= new StringBuilder();
        ret.append("-----------------------\n");
        ret.append("-    Lista punktow    -\n");
        ret.append("-----------------------\n");
        if (lista.size() == 0){
            ret.append("Brak punktow na liscie.");
        }else{
            for (PunktTeleportacji punkt : lista){
                String linia="";
                linia += "nazwa : "+punkt.getNazwa();
                linia += "   ";
                linia += " x: " + Math.round(punkt.getX());
                linia += " y: " + Math.round(punkt.getY());
                linia += " z: " + Math.round(punkt.getZ());
                linia += "\n";
                ret.append(linia);
            }
        }
        ret.append("-----------------------");
        return ret.toString();
    };

    /**
     * Znajdź index na liście dla wskazanej nazwy
     * @param nazwaPunktu -szukana nazwa punktu
     * @return index dla szukanej nazwy, jeśli błąd , zwróć -1
     */
    int indexNaLiscie(String nazwaPunktu){
        for (int i=0; i<lista.size();i++){
            PunktTeleportacji punkt = lista.get(i);
            if (punkt.getNazwa().equalsIgnoreCase(nazwaPunktu)){
                return i; // zwroc index z listy
            }
        }
        return -1; // nie znaleziono - blad
    }

    /**
     * Dodaj nowy punkt do listy
     * @param nowyPunkt utworzony wcześniej punkt
     */
    void dodaj(PunktTeleportacji nowyPunkt) {
        lista.add(nowyPunkt);
    }

    /**
     * Usun wskazany punkt z listy na podstawie indexu. Funkcja sprawdza czy podana wartość
     * mieści się w zakresie
     * @param index - wskazany index
     */
    void usun(int index){
        if (index < 0) return; // index nie moze byc mniejszy od 0
        if (index >= lista.size()) return; //zabezpieczenie przed przekroczeniem
        lista.remove(index);
    }

    /**
     * Usuń punkt na podstawie nazwy. Funkcja sprawdza czy nazwa występuje na liście
     * @param nazwa - szukana , unikalna nazwa dla punktu
     * @return - String jako wynik operacji
     */
    String usun(String nazwa){
        int index = indexNaLiscie(nazwa);
        String ret = "";
        if (index == -1){
            ret = "(TPplugin) - Usun - nie znaleziono nazwy.";
        }else{
            ret = "(TPplugin) - usuwam index : " + index;
            usun(index); //faktyczne usuniecie
        }
        return  ret;
    }

    /**
     * Wczytaj dane z pliku punkty.json
     * @throws IOException bledy odczytu pliku
     */
    void wczytajPlik()throws IOException {
        Gson gson = new Gson();
        File file = new File(Tpp.getPlugin().getDataFolder().getAbsolutePath() + "/punkty.json");
        if (file.exists()){
            Reader reader = new FileReader(file);
            PunktTeleportacji[] nowePunkty = gson.fromJson(reader, PunktTeleportacji[].class);
            lista.addAll(Arrays.asList(nowePunkty));
            System.out.println("(TPplugin) Punkty wczytane.");
        }else{
            System.out.println("(TPplugin) - plik z punktami nie istnieje.");
        }
    }

    /**
     * Zapisz dane z list do pliku punkty.json , utwórz niezbędne katalogi
     * @throws IOException bledy zapisu pliku
     */
    void zapiszPlik()throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        //Gson gson = new Gson();
        File file = new File(Tpp.getPlugin().getDataFolder().getAbsolutePath() + "/punkty.json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);

        gson.toJson(lista, writer);
        writer.flush();
        writer.close();
        System.out.println("(TPplugin) Punkty zapisane.");
    }

    /**
     * Wylosuj punkt z listy
     * @return wylosowany punkt
     */
    public PunktTeleportacji losujPunkt() {
        PunktTeleportacji ret = new PunktTeleportacji("","",0,0,0,0,0);
        Random generator = new Random();
        int ileElementow = lista.size();

        if (ileElementow == 0) {
            System.out.println("(TPplugin) - Lista elementow jest pusta, zwracam domyslny punkt 0,0,0 - bez opisu");
        } else {
            int wylosowanaLiczba = generator.nextInt(ileElementow);
            return lista.get(wylosowanaLiczba);
        }
        return ret;
    }

    public PunktTeleportacji wezPunkt(int index) {
        PunktTeleportacji ret = new PunktTeleportacji("","",0,0,0,0,0);

        if (index >= 0 && index <= lista.size() - 1) {
            ret = lista.get(index);
        } else {
            System.out.println("(TPplugin) - Wskazany index poza zakresem, zwracam domyslny punkt");
        }
        return ret;
    }

    public PunktTeleportacji wezPunkt(String nazwa){
        PunktTeleportacji ret = new PunktTeleportacji("","",0,0,0,0,0);
        int index = indexNaLiscie(nazwa);
        if (index > -1) ret = lista.get(index);
        return ret;
    }

    public void bazowePunkty(){
        System.out.println("(TPplugin) - tworze punkty bazowe.");
        lista.add(new PunktTeleportacji("p1","world",-204.51, -9.00,122.46,-318,-87));
        lista.add(new PunktTeleportacji("p2","world", -251.41,-16.00,95.34,(float) -759,0));
    }


}