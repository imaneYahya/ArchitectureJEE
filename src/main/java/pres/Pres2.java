package pres;

import dao.IDao;
import metier.IMetier;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Scanner;

public class Pres2 {
    public static void main(String[] args) throws Exception{
        Scanner scanner = new Scanner(new File("config.txt"));
        String daoClassNam=scanner.nextLine();
        Class cDao =Class.forName(daoClassNam);
        IDao dao=(IDao) cDao.newInstance();


        String metierClassName = scanner.nextLine();
        Class cMetier = Class.forName(metierClassName);
        IMetier metier = (IMetier) cMetier.newInstance();


        Method method = cMetier.getMethod("setDao",IDao.class);
        method.invoke(metier, dao);
        System.out.println("resultat => "+metier.calcul());

    }
}
