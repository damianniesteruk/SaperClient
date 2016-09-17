package saperclient.Network.Requests;

import com.google.gson.Gson;

import saperclient.Exceptions.*;
import saperclient.Network.*;

import javax.swing.JDialog;
import saperclient.View.Dialogs.*;
import saperclient.View.Frames.Login.*;
import saperclient.View.Frames.Menu.MenuFrame;
import saperclient.View.Frames.Register.FormRegisterPanel;

import saperclient.Model.Account;
import saperclient.SaperClient;

/**
 * @author Damian
 */
public class LoginNetRequest {
    
    public static void signIn() {
        
        JDialog dialog = new ProgressDialog( "Logowanie..." );
        String msg = "";
        
        Thread dialog_thread = new Thread() {
            
            @Override
            public void run() {
                
                if( dialog != null && !dialog.isVisible() )
                    dialog.setVisible( true );
            }
            
            @Override
            public void interrupt() {
                
                if( dialog != null ) {
                    dialog.setVisible( false );
                    dialog.removeAll();
                    dialog.dispose();
                }
            }
        };
        
        //----------------------------------------------------------------------
        
        try {
            
            Account account = new Account( FormLoginPanel.getLogin(), FormLoginPanel.getPassword() );

            if( account.getLogin().isEmpty() || account.getPassword().isEmpty() )
                throw new BlankLoginDataException();
            
            dialog_thread.start();
            
            if( SaperClient.client == null )
                SaperClient.client = new Client( SaperClient.SERVER_IP, SaperClient.SERVER_PORT );

            SaperClient.client.sendMsg( new NetRequest( "login", new Gson().toJson( account, Account.class ) ) );
            SaperClient.client.getMsgs();

        } catch( BlankLoginDataException e ) {
            msg = "Wypełnij wszystkie pola.";

        } catch( IncorrectLoginDataException e ) {
            msg = "Niepoprawne dane logowania.";

        } catch( Exception e ) {
            msg = "Problem z połączeniem.";
            e.printStackTrace();

        } finally {
            
            dialog_thread.interrupt();
            
            if( !msg.isEmpty() )
                new MessageDialog( msg, SaperClient.current_frame );
            else
                new MenuFrame();
        }
    }
    public static void register() {
        
        JDialog dialog = new ProgressDialog( "Rejestrowanie..." );
        String msg = "";
        
        Thread dialog_thread = new Thread() {
            
            @Override
            public void run() {
                
                if( dialog != null && !dialog.isVisible() )
                    dialog.setVisible( true );
            }
            
            @Override
            public void interrupt() {
                
                if( dialog != null ) {
                    dialog.setVisible( false );
                    dialog.removeAll();
                    dialog.dispose();
                }
            }
        };
        
        //----------------------------------------------------------------------
        
        try {
            
            Account account = new Account( FormRegisterPanel.getLogin(), FormRegisterPanel.getPassword() );

            if( account.getLogin().isEmpty() || account.getPassword().isEmpty() )
                throw new BlankLoginDataException();
            
            dialog_thread.start();
            
            if( SaperClient.client == null )
                SaperClient.client = new Client( SaperClient.SERVER_IP, SaperClient.SERVER_PORT );

            SaperClient.client.sendMsg( new NetRequest( "register", new Gson().toJson( account, Account.class ) ) );
            SaperClient.client.getMsgs();

        } catch( BlankLoginDataException e ) {
            msg = "Wypełnij wszystkie pola.";

        } catch( AccountExistException e ) {
            msg = "Konto już istnieje.";

        } catch( AccountRegisterFailedException ex ) {
            msg = "Błędne rejestrowanie konta.";
            
        } catch( Exception e ) {
            msg = "Problem z połączeniem.";
            e.printStackTrace();

        } finally {
            
            if( dialog_thread != null ) {
                
                dialog_thread.interrupt();
                dialog_thread = null;
            }
            
            if( !msg.isEmpty() )
                new MessageDialog( msg, SaperClient.current_frame );
            else
                new LoginFrame();
        }
    }
}
