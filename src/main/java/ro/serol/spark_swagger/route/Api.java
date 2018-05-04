package ro.serol.spark_swagger.route;

import io.swagger.annotations.*;
import ro.serol.spark_swagger.model.ApiError;
import ro.serol.spark_swagger.model.BloodType;
import ro.serol.spark_swagger.model.User;
import ro.serol.spark_swagger.model.request.CreateUserRequest;
import ro.serol.spark_swagger.util.JsonTransformer;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

@io.swagger.annotations.Api
@SwaggerDefinition(host = "localhost:4567", //
    info = @Info(description = "DonateAPP API", //
        version = "V1.0", //
        title = "Some random api for testing", //
        contact = @Contact(name = "Serol", url = "https://serol.ro") ) , //
    schemes = { SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS }, //
    consumes = { "application/json" }, //
    produces = { "application/json" }, //
    tags = { @Tag(name = "swagger") })
@Path("/")
@Produces("application/json")
public class Api {

    private static Api api = null;

    private Api() {
        createUser();
        donate();
        getAllUsers();
        getUser();
    }

    public static void setupRoutes() {
        if (api == null) {
            api = new Api();
        }
    }

    @GET
    @Path("/users")
    @ApiOperation(value = "Gets all user details and donations", nickname = "GetAllUsersRoute")
    @ApiImplicitParams({ //
        @ApiImplicitParam(required = true, dataType = "string", name = "auth", paramType = "header"), //
        @ApiImplicitParam(required = false, dataType = "string", name = "id", paramType = "query") //
    }) //
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success", response = User.class, responseContainer = "List"), //
        @ApiResponse(code = 400, message = "Invalid input data", response = ApiError.class), //
        @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class), //
        @ApiResponse(code = 404, message = "User not found", response = ApiError.class) //
    })
    public void getAllUsers() {
        get("/users", (request, response) -> {
            String paramId = request.queryParams("id");
            System.out.printf("Path %s - parameter %s\n", "/users", paramId);
            User user1 = makeUser("1");
            User user2 = makeUser("2");
            List<User> users = new LinkedList<User>();
            users.add(user1);
            users.add(user2);
            response.type("application/json");
            return new JsonTransformer().render(users);
        });
    }

    @GET
    @Path("/users/{id}")
    @ApiOperation(value = "Gets user details and donations", nickname = "GetUserRoute")
    @ApiImplicitParams({ //
        @ApiImplicitParam(required = true, dataType = "string", name = "auth", paramType = "header"), //
        @ApiImplicitParam(required = true, dataType = "string", name = "id", paramType = "path") //
    }) //
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success", response = User.class), //
        @ApiResponse(code = 400, message = "Invalid input data", response = ApiError.class), //
        @ApiResponse(code = 401, message = "Unauthorized", response = ApiError.class), //
        @ApiResponse(code = 404, message = "User not found", response = ApiError.class) //
    })
    public void getUser() {
        get("/users/:id", (request, response) -> {
            String paramId = request.params("id");
            System.out.printf("Path %s - parameter %s\n", "/users/{id}", paramId);

            User user = makeUser(paramId);
            response.type("application/json");
            return new JsonTransformer().render(user);
        });
    }

    private User makeUser(String userId) {
        User user = new User();
        user.setBloodType(BloodType.ABNeg);
        user.setDonations(new ArrayList<>());
        user.setId(Integer.parseInt(userId));
        user.setLastname("Lastname");
        user.setName("Firstname");
        user.setPhoneNumber("555-555-5555");

        return user;
    }

    @POST
    @Path("/users/{id}/donate")
    @ApiOperation(value = "Creates a donation request for the user", nickname="DonateRoute")
    @ApiImplicitParams({ //
        @ApiImplicitParam(required = true, dataType = "string", name = "auth", paramType = "header"), //
        @ApiImplicitParam(required = true, dataType = "string", name = "id", paramType = "path") //
    }) //
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success"), //
        @ApiResponse(code = 400, message = "Invalid input data", response=ApiError.class), //
        @ApiResponse(code = 401, message = "Unauthorized", response=ApiError.class), //
        @ApiResponse(code = 404, message = "User not found", response=ApiError.class) //
    })
    public void donate() {
        post("/users/:id/donate", (request, response) -> {
            return new JsonTransformer().render(Boolean.TRUE);
        });
    }

    @POST
    @Path("/users")
    @ApiOperation(value = "Creates a new user", nickname="CreateUserRoute")
    @ApiImplicitParams({ //
        @ApiImplicitParam(required = true, dataType="string", name="auth", paramType = "header"), //
        @ApiImplicitParam(required = true, dataType = "ro.serol.spark_swagger.model.request.CreateUserRequest", paramType = "body") //
    }) //
    @ApiResponses(value = { //
        @ApiResponse(code = 200, message = "Success", response=User.class), //
        @ApiResponse(code = 400, message = "Invalid input data", response=ApiError.class), //
        @ApiResponse(code = 401, message = "Unauthorized", response=ApiError.class), //
        @ApiResponse(code = 404, message = "User not found", response=ApiError.class) //
    })
    public void createUser() {
        post("/users", (request, response) -> {
            String body = request.body();
            CreateUserRequest cur = new JsonTransformer().deserialize(body, CreateUserRequest.class);

            User user = new User();
            user.setPhoneNumber(cur.getPhoneNumber());
            user.setName(cur.getName());
            user.setLastname(cur.getLastname());
            user.setId(99);
            user.setBloodType(cur.getBloodType());

            return new JsonTransformer().render(user);
        });
    }

}
