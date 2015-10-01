package developer.sigmamovil.sigmatrackalpha1.io;

/**
 * Created by william.montiel on 14/09/2015.
 */
public class ApiConstants {
    public static final String URL_BASE = "http://track.sigmamovil.com";
    public static final String API = "api";
    public static final String GET_HISTORY = "gethistory";
    public static final String POST_LOGIN = "validatelogin";
    public static final String CLIENTS = "getclients";
    public static final String VISIT_TYPES = "getvisittypes";
    public static final String CLIENT_AND_VISIT_TYPES = "getclientsandvisittypes";
    public static final String NEW_VISIT = "newvisit";
    public static final String CLOSE_VISIT = "closevisit";
    public static final String ADD_OBSERVATION = "addobservation";
    public static final String NEW_CLIENT = "createnewclient";

    public static final String URL_VISIT_HISTORY = "/" + API + "/" + GET_HISTORY;
    public static final String URL_CLIENTS = "/" + API + "/" + CLIENTS + "/";
    public static final String URL_VISIT_TYPES = "/" + API + "/" + VISIT_TYPES + "/";
    public static final String URL_CLIENT_AND_VISIT_TYPES = "/" + API + "/" + CLIENT_AND_VISIT_TYPES + "/";
    public static final String URL_NEW_VISIT = "/" + API + "/" + NEW_VISIT;
    public static final String URL_CLOSE_VISIT = "/" + API + "/" + CLOSE_VISIT;
    public static final String URL_ADD_OBSERVATION = "/" + API + "/" + ADD_OBSERVATION;
    public static final String URL_NEW_CLIENT = "/" + API + "/" + NEW_CLIENT;

    public static final String COMPLETE_URL_POST_LOGIN = URL_BASE + "/" + API + "/" + POST_LOGIN;
    public static final String COMPLETE_URL_CLIENTS = URL_BASE +  URL_CLIENTS;
    public static final String COMPLETE_URL_VISIT_TYPES = URL_BASE +  URL_VISIT_TYPES;
    public static final String COMPLETE_URL_CLIENTS_AND_VISIT_TYPES = URL_BASE +  URL_CLIENT_AND_VISIT_TYPES;
    public static final String COMPLETE_URL_NEW_VISIT = URL_BASE +  URL_NEW_VISIT;
    public static final String COMPLETE_URL_CLOSE_VISIT = URL_BASE +  URL_CLOSE_VISIT;
    public static final String COMPLETE_URL_ADD_OBSERVATION = URL_BASE +  URL_ADD_OBSERVATION;
    public static final String COMPLETE_URL_NEW_CLIENT = URL_BASE +  URL_NEW_CLIENT;
}