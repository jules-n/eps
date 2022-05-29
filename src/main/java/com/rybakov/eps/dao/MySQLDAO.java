package com.rybakov.eps.dao;

import com.rybakov.eps.models.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MySQLDAO<T, K> implements IDAO<T, K> {

    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    private Statement statement;

    public MySQLDAO(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/eps?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                "root", "1234");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Participant readParticipant(String login, String password) {
        String query = "SELECT * FROM `Participant` WHERE login = ? AND password = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,login);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return new Participant(
                        resultSet.getInt("idParticipant"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getBoolean("isOrganizer"),
                        resultSet.getLong("phone"),
                        resultSet.getString("login"),
                        resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(T entity){
        if(entity instanceof Participant) return createParticipant((Participant) entity);
        if(entity instanceof Event) return createEvent((Event) entity);
        else return false;
    }

    private boolean createEvent(Event entity){
        String query = "INSERT INTO Event(name, place, date,description,idStatus,idType,idOwner) VALUES(?, ?, ?, ?, ?, ?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, entity.getEventName());
            preparedStatement.setString(2, entity.getPlace());
            entity.getDate().setHours(entity.getDate().getHours()+3);
            preparedStatement.setTimestamp(3, new Timestamp(entity.getDate().getTime()));
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setInt(5, entity.getIdStatus());
            preparedStatement.setInt(6, entity.getIdType());
            preparedStatement.setInt(7, entity.getIdOwner());
            preparedStatement.execute();
            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean createParticipant(Participant participant){
        String query = "INSERT INTO Participant(name,surname, isOrganizer,phone,login,password) VALUES(?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, participant.getName());
            preparedStatement.setString(2, participant.getSurname());
            preparedStatement.setBoolean(3, participant.isOrganizer());
            preparedStatement.setLong(4, participant.getPhone());
            preparedStatement.setString(5, participant.getLogin());
            preparedStatement.setString(6, participant.getPassword());
            preparedStatement.execute();
            return true;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List readAllBy(T entity, K key){
        if(entity instanceof Event) return readAllByOwner((Integer) key);
        else if(entity instanceof Participant) return readAllExceptMe((Integer) key);
        else return null;
    }

    @Override
    public boolean update(T entity, K key) {
        if(entity instanceof Event) return updateEvent((Integer)key);
        else return false;
    }

    @Override
    public boolean archiveEvents(K key) {
        try {
            var callableStatement = connection.prepareCall("{call archive_old_events(?)}");
            callableStatement.setInt(1, (Integer) key);
            callableStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean create(int[] participants, int event, String text, int owner) {
        String query = "INSERT INTO Invitation(text, idEvent) VALUES(?, ?)";
        try {
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, text);
            preparedStatement.setInt(2, event);
            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();
        }
        catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        //query = "INSERT INTO ParticipantsInvitation(idParticipant, idInvitation, isAccepted) VALUES(?,?,?)";
        try{
            if(resultSet.next()){
/*            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, owner);
            preparedStatement.setInt(2, resultSet.getInt(1));
            preparedStatement.setBoolean(3,true);
            preparedStatement.execute();*/
            query = "INSERT INTO ParticipantsInvitation(idParticipant, idInvitation) VALUES(?,?)";
            for(int i:participants){
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, i);
                preparedStatement.setInt(2, resultSet.getInt(1));
                preparedStatement.execute();
            }
            }

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Invitation> readMyInvitation(K key) {
        List<Invitation> list = new ArrayList<>();
        String query = "SELECT `Invitation`.`idInvitation`, `Invitation`.`text`, `Invitation`.`idEvent`, isAccepted FROM `ParticipantsInvitation` INNER JOIN `Invitation` ON `Invitation`.`idInvitation` = `ParticipantsInvitation`.`idInvitation` INNER JOIN Event ON Event.idEvent = Invitation.idEvent WHERE `ParticipantsInvitation`.`idParticipant` = ? AND (isAccepted IS NULL OR isAccepted=1) AND Event.idStatus = 1";
        List<Integer> okevent = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, (Integer)key);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                okevent.add(resultSet.getInt(3));
                list.add(new Invitation(resultSet.getInt(1),resultSet.getString(2),null,null, resultSet.getBoolean(4)));
            }
            for(int i = 0 ; i<list.size(); i++){
                list.get(i).setEvent(readEvent(okevent.get(i)));
            }
            for(int i = 0 ; i<list.size(); i++){
                list.get(i).setOrganizer(readParticipant(list.get(i).getEvent().getIdOwner()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.err.println(list.size());
        return list;
    }

    @Override
    public boolean confirm(int user, K key, Boolean decision) {
        String query = "UPDATE `ParticipantsInvitation` SET `isAccepted` = ? WHERE `ParticipantsInvitation`.`idParticipant` = ? AND `ParticipantsInvitation`.`idInvitation` = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, decision);
            preparedStatement.setInt(2,user);
            preparedStatement.setInt(3,(Integer) key);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean updateEvent(Integer key){
        String query = "UPDATE Event SET idStatus = '2' WHERE `Event`.`idEvent` = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,key);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<Event> readAllByOwner(Integer id){
        List<Event> list = new ArrayList<>();
        String  query = "SELECT * FROM Event WHERE idStatus <> 3 AND `idOwner` = ? ";
        //SELECT * FROM Event WHERE `date`>=NOW() AND idStatus=1 AND idEvent NOT IN(SELECT idInvitation FROM Invitation) AND `idOwner` = ?
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,id);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    java.util.Date date = dateFormat.parse(resultSet.getTimestamp("date").toString());
                    list.add(new Event(resultSet.getInt("idEvent"),resultSet.getString("name"),resultSet.getString("place"),
                            date,resultSet.getString("description"),resultSet.getInt("idStatus"),
                            resultSet.getInt("idType"),resultSet.getInt("idOwner")));
                }
            } catch (SQLException | ParseException e) {
                e.printStackTrace();
            }
            return list;
    }

    @Override
    public List readAll(T entity) {
        if(entity instanceof Rate) return readAllRate();
        //else if(entity instanceof Participant) return readAllParticipants();
        else if(entity instanceof Type) return readAllType();
        else return null;
    }

    private List<Type> readAllType(){
        List<Type> list = new ArrayList<Type>();
        String query = "SELECT * FROM TypeOfEvent";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next())
                list.add(new Type(resultSet.getInt("idType"),resultSet.getString("typeName")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    private List<Rate> readAllRate(){
        List<Rate> list = new ArrayList<Rate>();
        String query = "SELECT * FROM Rate";
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                list.add(new Rate(resultSet.getString("accountType"),resultSet.getFloat("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Participant> readAllExceptMe(int id){
        List<Participant> list = new ArrayList<>();
        String query = "SELECT * FROM Participant WHERE idParticipant<>?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                list.add(
                        new Participant(
                                resultSet.getInt("idParticipant"),
                                resultSet.getString("name"),
                                resultSet.getString("surname"),
                                resultSet.getBoolean("isOrganizer"),
                                resultSet.getLong("phone"),
                                resultSet.getString("login"),
                                resultSet.getString("password"))
                );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Event readEvent(int id){
        String query = "SELECT * FROM Event WHERE idEvent = ?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                java.util.Date date = dateFormat.parse(resultSet.getTimestamp("date").toString());
                return new Event(resultSet.getInt("idEvent"),resultSet.getString("name"),resultSet.getString("place"),
                        date,resultSet.getString("description"),resultSet.getInt("idStatus"),
                        resultSet.getInt("idType"),resultSet.getInt("idOwner"));
            }
        }
        catch (SQLException | ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public Participant readParticipant(int id){
        String query = "SELECT * FROM Participant WHERE idParticipant = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return new Participant(
                        id,
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getBoolean("isOrganizer"),
                        resultSet.getLong("phone"),
                        resultSet.getString("login"),
                        resultSet.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
