using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Odysseus_Remote_Client.Model;

namespace Odysseus_Remote_Client
{
    public partial class MainForm : Form
    {
        public MainForm()
        {
            InitializeComponent();
        }

        private void beendenToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void verbindungHerstellenToolStripMenuItem_Click(object sender, EventArgs e)
        {
            this.toolStripStatusLabel1.Text = "Baue Verbindung zu Odysseus auf!";
            OdysseusWebservice.getInstance().connect("System", "manager");           
            isConnected();
        }

        private void verbindungTrennenToolStripMenuItem_Click(object sender, EventArgs e)
        {
            OdysseusWebservice.getInstance().disconnect();
            isDisconnected();
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
            isDisconnected();
        }


        private void isConnected()
        {
            verbindungHerstellenToolStripMenuItem.Enabled = false;
            verbindungTrennenToolStripMenuItem.Enabled = true;
            this.toolStripStatusLabel1.Text = "Verbindung zu Odysseus hergestellt";
            mainPanel.Visible = true;
            mainPanel.Enabled = true;
            synchronizeWithOdysseus();
        }

        private void isDisconnected()
        {
            verbindungHerstellenToolStripMenuItem.Enabled = true;
            verbindungTrennenToolStripMenuItem.Enabled = false;
            this.toolStripStatusLabel1.Text = "Verbindung zu Odysseus ist getrennt";
            mainPanel.Visible = false;
            mainPanel.Enabled = false;
        }

        private void synchronizeWithOdysseus()
        {
            if (OdysseusWebservice.getInstance().isConnected())
            {
                loadSources();
                loadQueries();
            }
        }


        private void loadSources()
        {
            sourcesListBox.Items.Clear();
            List<string> sources = OdysseusWebservice.getInstance().getSources();
            if (sources != null)
            {
                foreach (String s in sources)
                {
                    sourcesListBox.Items.Add(s);
                }
            }
        }

        private void loadQueries()
        {
            queriesListBox.Items.Clear();
            List<string> queries = OdysseusWebservice.getInstance().getQueries();
            if (queries != null)
            {
                foreach (String s in queries)
                {
                    queriesListBox.Items.Add(s);
                }
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (OdysseusWebservice.getInstance().addQuery(this.queryTextBox.Text))
            {
                MessageBox.Show("Query successfully added", "Success", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            else
            {
                MessageBox.Show("Query could not be added!", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
            loadQueries();
        }

    }
}
