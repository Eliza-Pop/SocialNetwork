package Repository.Db;

import Excepti.RepositoryException;

import java.sql.SQLException;

final class DbErrors {
    static final String UNIQUE_VIOLATION = "23505";
    private DbErrors()
    {

    }

    static boolean has(SQLException e, String sqlState)
    {
        return sqlState.equals(e.getSQLState());
    }

    static RepositoryException translate(SQLException e, String operation)
    {
        String state;
        String message;
        if(e.getSQLState()==null)
        {
            state = "";
        }
        else
        {
            state = e.getSQLState();
        }
        if(state.isEmpty() || state.startsWith("08"))
        {
            message = "Nu se poate comunica cu baza de date !";
        }
        else
        {
            if(state.startsWith("28"))
            {
                message = "Autentificare esuata la baza de date !";
            }
            else
            {
                if(state.startsWith("3D000"))
                {
                    message = "Baza de date nu exista !";
                }
                else
                {
                    if(state.startsWith("42P01"))
                    {
                        message = "Tabela cautata nu exista in baza de date !";
                    }
                    else
                    {
                        if(state.startsWith("23505"))
                        {
                            message = "Valoarea deja exista in baza de date !";
                        }
                        else
                        {
                            if(state.startsWith("23503"))
                            {
                                message = "Operatia face referire la un utilizator ce nu exista !";
                            }
                            else
                            {
                                if(state.startsWith("23514"))
                                {
                                    message = "Datele nu respecta o ordine a bazei de date !";
                                }
                                else
                                {
                                    if(state.startsWith("23502"))
                                    {
                                        message = "Datele nu pot fi nule !";
                                    }
                                    else
                                    {
                                        message ="Eroare la accesarea bazei de date !";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return  new RepositoryException(message,e);
    }
}
