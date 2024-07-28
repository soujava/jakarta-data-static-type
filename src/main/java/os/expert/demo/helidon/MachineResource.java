package os.expert.demo.helidon;


import jakarta.data.Order;
import jakarta.data.Sort;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.jnosql.mapping.Database;
import org.eclipse.jnosql.mapping.DatabaseType;

import java.util.List;
import java.util.logging.Logger;

@Path("/machines")
@ApplicationScoped
public class MachineResource {

    private static final Logger LOGGER = Logger.getLogger(MachineResource.class.getName());

    public static final Order<Machine> ORDER_MANUFACTURER = Order.by(Sort.asc("manufacturer"));

    private final MachineRepository repository;

    @Inject
    public MachineResource(@Database(DatabaseType.DOCUMENT) MachineRepository repository) {
        this.repository = repository;
    }

    @GET
    public List<Machine> getMachines(@QueryParam("page") @DefaultValue("1") int page,@QueryParam("page_size") @DefaultValue("10") int pageSize) {
        LOGGER.info("Get machines from page " + page + " with page size " + pageSize);
        Page<Machine> machines = this.repository.findAll(PageRequest.ofPage(page).size(pageSize), ORDER_MANUFACTURER);
        return machines.content();
    }

    @GET
    @Path("gas")
    public List<Machine> getGasMachines() {
        return this.repository.findByType("gas");
    }

    @GET
    @Path("electric")
    public List<Machine> getElectricMachines() {
        return this.repository.findByType("electric");
    }

    @GET
    @Path("{id}")
    public Machine get(@PathParam("id") String id) {
        LOGGER.info("Get machine by id " + id);
        return this.repository.findById(id)
                .orElseThrow(() -> new WebApplicationException("Machine not found with id: " + id, Response.Status.NOT_FOUND));
    }


    @PUT
    public void save(Machine machine) {
        LOGGER.info("Saving a machine " + machine);
        this.repository.save(machine);
    }
}
