1-   La création de l'interface IDao avec une méthode getDate

  package dao;

public interface IDao {
        double getData();
}

2-  la création d’une implémentation de cette interface 
package dao;

import org.springframework.stereotype.Component;

@Component("dao")
public class DaoImpl implements IDao{
    @Override
    public double getData() {
        // se conneceter a la base de donnees pour recuperer la temperature
        System.out.println("version BDD");
        double temp =  Math.random()*40;
        return temp;
    }
}


3-  La création de l'interface IMetier avec une méthode calcul
package metier;

public interface IMetier{
    double calcul();
}

4-  La création d’une implémentation de cette interface en utilisant le couplage faible
package metier;

import dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("metier")
public class MetierImpl implements IMetier {
    @Autowired
    private IDao dao;//Couplage faible

    public MetierImpl(IDao dao) {
        this.dao = dao;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }

    @Override
    public double calcul() {
        double tmp=dao.getData();
        double res = tmp*600/Math.cos(tmp*Math.PI);
        return res;
    }
}

5-  l'injection des dépendances :
Maintenant afin d’assurer la communication entre la classe ImetierImpl et Dao, il faut injecter la dépendance. Ceci peut se réaliser à travers 2 méthodes :
a.	Par instanciation statique :
b.	package pres;

import ext.DaoImpl2;
import metier.MetierImpl;

public class Presentation {
    public static void main(String[] args) {
        // injection des dependances par instantiation statique
      DaoImpl2 dao = new DaoImpl2();
        MetierImpl metier = new MetierImpl(dao);
        //metier.setDao(dao);
        System.out.println("Resultat =" +metier.calcul());
    }
}

c.	Par instanciation dynamique :

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

d.	En utilisant le Framework Spring :
•	Version XML :
•	package pres;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PresSpringXML {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        IMetier metier =(IMetier) context.getBean("metier");
        System.out.println(metier.calcul());
    }
}

Voici la structure du fichier applicationContext.XML :
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
<bean id="dao" class="ext.DaoImpl2"></bean>
    <bean id="metier" class="metier.MetierImpl">
        <constructor-arg ref="dao"></constructor-arg>
    </bean>

</beans>

•	Version Annotations :

package pres;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PresSpringAnnotation {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext("dao","metier");
        IMetier metier = context.getBean(IMetier.class);
        System.out.println(metier.calcul());
    }
}











