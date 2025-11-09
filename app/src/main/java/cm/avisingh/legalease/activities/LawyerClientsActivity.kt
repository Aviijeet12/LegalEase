package cm.avisingh.legalease.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cm.avisingh.legalease.databinding.ActivityLawyerClientsBinding
import cm.avisingh.legalease.models.Client
import cm.avisingh.legalease.adapters.ClientsAdapter

class LawyerClientsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLawyerClientsBinding
    private lateinit var clientsAdapter: ClientsAdapter
    private val clientsList = mutableListOf<Client>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLawyerClientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadDummyClients()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "My Clients"
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        clientsAdapter = ClientsAdapter(clientsList) { client ->
            // Handle client click
        }
        binding.recyclerViewClients.apply {
            layoutManager = LinearLayoutManager(this@LawyerClientsActivity)
            adapter = clientsAdapter
        }
    }

    private fun loadDummyClients() {
        clientsList.clear()

        clientsList.addAll(listOf(
            Client(
                id = "client1",
                name = "Rajesh Kumar",
                email = "rajesh.kumar@email.com",
                phone = "+91 98765 43210",
                activeCases = 2,
                totalCases = 5,
                status = "Active"
            ),
            Client(
                id = "client2",
                name = "Priya Sharma",
                email = "priya.sharma@email.com",
                phone = "+91 98765 43211",
                activeCases = 1,
                totalCases = 3,
                status = "Active"
            ),
            Client(
                id = "client3",
                name = "Amit Gupta",
                email = "amit.gupta@email.com",
                phone = "+91 98765 43212",
                activeCases = 1,
                totalCases = 2,
                status = "Active"
            ),
            Client(
                id = "client4",
                name = "Suresh Reddy",
                email = "suresh.reddy@email.com",
                phone = "+91 98765 43213",
                activeCases = 1,
                totalCases = 1,
                status = "Active"
            ),
            Client(
                id = "client5",
                name = "Vijay Singh",
                email = "vijay.singh@email.com",
                phone = "+91 98765 43214",
                activeCases = 1,
                totalCases = 4,
                status = "Active"
            ),
            Client(
                id = "client6",
                name = "Anil Mehta",
                email = "anil.mehta@email.com",
                phone = "+91 98765 43215",
                activeCases = 1,
                totalCases = 2,
                status = "Active"
            ),
            Client(
                id = "client7",
                name = "Global Tech Pvt Ltd",
                email = "info@globaltech.com",
                phone = "+91 98765 43216",
                activeCases = 1,
                totalCases = 6,
                status = "Active"
            ),
            Client(
                id = "client8",
                name = "Meera Devi",
                email = "meera.devi@email.com",
                phone = "+91 98765 43217",
                activeCases = 1,
                totalCases = 1,
                status = "Active"
            ),
            Client(
                id = "client9",
                name = "Rahul Verma",
                email = "rahul.verma@email.com",
                phone = "+91 98765 43218",
                activeCases = 0,
                totalCases = 3,
                status = "Inactive"
            ),
            Client(
                id = "client10",
                name = "Sunita Patel",
                email = "sunita.patel@email.com",
                phone = "+91 98765 43219",
                activeCases = 2,
                totalCases = 4,
                status = "Active"
            )
        ))

        clientsAdapter.notifyDataSetChanged()
    }
}
