package ro.serol.spark_swagger.route;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import ro.serol.spark_swagger.model.ApiError;
import ro.serol.spark_swagger.model.User;
import spark.Request;
import spark.Response;
import spark.Route;

@Api
@Path("/user")
@Produces("application/json")
public class GetAllUsersRoute implements Route {

	@GET
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
	public List<User> handle(@ApiParam(hidden = true) Request request, @ApiParam(hidden = true) Response response) throws Exception {
		String paramId = request.queryParams("id");
		System.out.printf("Path %s - parameter %s\n", GetAllUsersRoute.class.getName(), paramId);
		return new LinkedList<User>();
	}

}
