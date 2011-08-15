using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Odysseus_Remote_Client.OdysseusService;

namespace Odysseus_Remote_Client.Model
{
    public class OdysseusWebservice
    {
        private static OdysseusWebservice instance = null;

        private WebserviceServerClient client;

        private OdysseusWebservice()
        {

        }

        public static OdysseusWebservice getInstance()
        {
            if (instance == null)
            {
                instance = new OdysseusWebservice();
            }
            return instance;
        }

        public string SecurityToken { get; set; }

        public Boolean isConnected()
        {
            if (this.SecurityToken != null)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        public void connect(String username, String password)
        {
            client = new WebserviceServerClient();
            stringResponse cr = client.login(username, password);
            if (cr.successful)
            {
                SecurityToken = cr.responseValue;
            }
            else
            {
                SecurityToken = null;
                client = null;
            }
        }

        public void disconnect()
        {
            this.SecurityToken = null;
            this.client = null;
        }

        public List<String> getSources()
        {
            List<String> sources = new List<String>();
            if (isConnected())
            {
                stringListResponse cr = this.client.getInstalledSources(SecurityToken);
                if (cr.responseValue != null)
                {
                    foreach (String s in cr.responseValue)
                    {
                        sources.Add(s);
                    }
                }
            }
            return sources;
        }


        public List<String> getQueries()
        {
            List<String> queries = new List<String>();
            if (isConnected())
            {
                stringListResponse res = this.client.getInstalledQueries(SecurityToken);
                if (res.responseValue != null)
                {
                    foreach (String s in res.responseValue)
                    {
                        queries.Add(s);
                    }
                }
            }
            return queries;
        }

        public bool addQuery(String query)
        {
            if (isConnected())
            {
                response r = this.client.addQuery(SecurityToken, "CQL", query, "Standard");
                return r.successful;
            }
            return false;
        }
    }
}
