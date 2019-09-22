package org.jugvale.certificate.generator.rest;

import java.util.List;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jugvale.certificate.generator.model.CertificateModel;

/**
 * CertificateModelResource
 */
@Path("certificate-model")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CertificateModelResource {

  @QueryParam("size")
  @DefaultValue("10")
  int pageSize;
  @QueryParam("page")
  @DefaultValue("0")
  int page;

  @GET
  public List<CertificateModel> all() {
    return CertificateModel.findAll().page(page, pageSize).list();
  }
  
  @PUT
  @Transactional
  public Response create(CertificateModel model, @Context UriInfo uriInfo) {
      CertificateModel.persist(model);
      UriBuilder uriBuilder = UriBuilder.fromResource(CertificateModelResource.class);
      uriBuilder.path(model.id.toString());
      return Response.created(uriBuilder.build()).entity(model.id).build();
  }
  
  @DELETE
  @Path("{id}")
  @Transactional
  public void remove(@PathParam("id") long id) {
      CertificateModel.delete("id", id);
  }
  

}