package connection;

import java.sql.Connection;

/*
 *    Interfejs, który ma na celu zagwarantowanie określonej struktury dla klas, które będą zawierały implementację
 *    połączenia z różnymi typami serwerów baz danych. Jako efekt końcowy połączenia w JDBC chcemy uzyskać obiekt
 *    Connection niezależnie od tego z jaką bazą będziemy się łączyć (np. MySQL, Oracle czy H2).
 */
public interface CustomConnection {

    /*
     *   Chcemy zagwarantować, że że obiekt Connection z klasy implementującej połączenie do bazy danych możemy otrzymać
     *   poprzez wywołanie metody getConnection niezależnie od typu serwera do jakiego będziemy się łączyć
     */
    Connection getConnection();
}
