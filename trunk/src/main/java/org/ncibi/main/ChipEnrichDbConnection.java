package org.ncibi.main;


import java.io.IOException;
import org.hibernate.Session;
import org.ncibi.db.EntityManagers;
import org.ncibi.db.PersistenceSession;
import org.ncibi.db.PersistenceUnit;
import org.ncibi.db.ws.ChipEnrichUrlLinkName;
import org.ncibi.hibernate.SessionProcedure;
import org.ncibi.hibernate.Sessions;

public class ChipEnrichDbConnection {
  public static void main(String[] args) throws IOException
		{
			ChipEnrichDbConnection jt = new ChipEnrichDbConnection();
			jt.saveUuid("feb5test1", "11ac-48d4-9008-6f39a0746a72","inspiresnehal@gmail.com");
			jt.getEmail("11ac-48d4-9008-6f39a0746a72");
			
		}

  PersistenceSession persistence = new PersistenceUnit(EntityManagers
                .newEntityManagerFromProject("task"));

  public void saveUuid( String name , String uuid, String email)
		{
	  System.out.println("Inside Db connection and with name ="+ name+ " uid"+ uuid);
		final ChipEnrichUrlLinkName linkname = new ChipEnrichUrlLinkName();
        linkname.setUuid(uuid);
        linkname.setName(name);
        linkname.setEmail(email);
        Sessions.withSession(persistence.session(), new SessionProcedure()
        {
            @Override
            public void apply(Session session)
            {
                session.saveOrUpdate(linkname);
            }
        });
   	
		}

  public String getUuid ( String name)
  {
		  final String hql = "from ws.ChipEnrichUrlLinkName where name = '" + name+ "'";
		  ChipEnrichUrlLinkName  link = null;
		  String uuid = null;
		
		  try
		  {
		      link = persistence.hqlQuery(hql).single();
		       uuid = link.getUuid();
		  }
		  catch (Exception e)
		  {
		      link = null;
		  }
		  System.out.println(link.getUuid());
		  return uuid;
  }
  
  public String getEmail ( String uuid)
  {
		  final String hql = "from ws.ChipEnrichUrlLinkName where uuid = '" + uuid+ "'";
		  ChipEnrichUrlLinkName  link = null;
		  String email = null;
		
		  try
		  {
		      link = persistence.hqlQuery(hql).single();
		       email = link.getEmail();
		  }
		  catch (Exception e)
		  {
		      link = null;
		  }
		  System.out.println(link.getEmail());
		  return email;
  }

  public String getOutname ( String uuid)
  {
		  final String hql = "from ws.ChipEnrichUrlLinkName where uuid = '" + uuid+ "'";
		  ChipEnrichUrlLinkName  link = null;
		  String outname = null;
		
		  try
		  {
		      link = persistence.hqlQuery(hql).single();
		       outname = link.getName();
		  }
		  catch (Exception e)
		  {
		      link = null;
		  }
		  System.out.println(link.getName());
		  return outname;
  }

}
  

		
		
		