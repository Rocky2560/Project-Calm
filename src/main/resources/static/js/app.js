// Data Store
const store = {
    clients: JSON.parse(localStorage.getItem('clients')) || [],
    tasks: JSON.parse(localStorage.getItem('tasks')) || [],
    payments: JSON.parse(localStorage.getItem('payments')) || [],
    amlChecks: JSON.parse(localStorage.getItem('amlChecks')) || []
};

// Save to localStorage
function saveData() {
    localStorage.setItem('clients', JSON.stringify(store.clients));
    localStorage.setItem('tasks', JSON.stringify(store.tasks));
    localStorage.setItem('payments', JSON.stringify(store.payments));
    localStorage.setItem('amlChecks', JSON.stringify(store.amlChecks));
}

// Navigation
document.querySelectorAll('.nav-link').forEach(link => {
    link.addEventListener('click', (e) => {
        e.preventDefault();
        const page = link.dataset.page;

        document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
        link.classList.add('active');

        document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
        document.getElementById(`page-${page}`).classList.add('active');
    });
});

// Render Functions
function renderClients() {
    const container = document.getElementById('clientList');
    const search = document.getElementById('clientSearch').value.toLowerCase();

    const filtered = store.clients.filter(c =>
        c.name.toLowerCase().includes(search) ||
        (c.email && c.email.toLowerCase().includes(search))
    );

    if (filtered.length === 0) {
        container.innerHTML = '<div class="empty-state">No clients found. Add your first client!</div>';
        return;
    }

    container.innerHTML = filtered.map(client => `
    <div class="list-item">
      <div class="list-item-info">
        <h4>${client.name}</h4>
        <p>${client.email || 'No email'} ${client.phone ? '• ' + client.phone : ''}</p>
        <div class="list-item-meta">
          <span class="status ${client.status}">${client.status}</span>
          ${(client.services || []).map(s => `<span class="tag">${s}</span>`).join('')}
        </div>
      </div>
      <div class="list-item-actions">
        <button onclick="editClient('${client.id}')">✏️</button>
        <button onclick="deleteClient('${client.id}')">🗑️</button>
      </div>
    </div>
  `).join('');

    document.getElementById('clientCount').textContent = store.clients.length;
}

function renderTasks() {
    const container = document.getElementById('taskList');

    if (store.tasks.length === 0) {
        container.innerHTML = '<div class="empty-state">No tasks yet. Create your first task!</div>';
        return;
    }

    container.innerHTML = store.tasks.map(task => {
        const client = store.clients.find(c => c.id === task.clientId);
        return `
      <div class="list-item">
        <div class="list-item-info">
          <h4>${task.title}</h4>
          <p>${client ? client.name : 'No client'} ${task.deadline ? '• Due: ' + new Date(task.deadline).toLocaleDateString() : ''}</p>
          <div class="list-item-meta">
            <span class="status ${task.status}">${task.status}</span>
            ${task.serviceType ? `<span class="tag">${task.serviceType}</span>` : ''}
          </div>
        </div>
        <div class="list-item-actions">
          <button onclick="toggleTaskStatus('${task.id}')">${task.status === 'completed' ? '↩️' : '✓'}</button>
          <button onclick="deleteTask('${task.id}')">🗑️</button>
        </div>
      </div>
    `;
    }).join('');
}

function renderPayments() {
    const container = document.getElementById('paymentList');

    if (store.payments.length === 0) {
        container.innerHTML = '<div class="empty-state">No payments recorded.</div>';
        return;
    }

    container.innerHTML = store.payments.map(payment => {
        const client = store.clients.find(c => c.id === payment.clientId);
        return `
      <div class="list-item">
        <div class="list-item-info">
          <h4>£${payment.amount.toFixed(2)}</h4>
          <p>${client ? client.name : 'No client'} ${payment.description ? '• ' + payment.description : ''}</p>
          <div class="list-item-meta">
            <span class="status ${payment.status}">${payment.status}</span>
            ${payment.dueDate ? `<span class="tag">Due: ${new Date(payment.dueDate).toLocaleDateString()}</span>` : ''}
          </div>
        </div>
        <div class="list-item-actions">
          <button onclick="togglePaymentStatus('${payment.id}')">${payment.status === 'paid' ? '↩️' : '✓'}</button>
          <button onclick="deletePayment('${payment.id}')">🗑️</button>
        </div>
      </div>
    `;
    }).join('');
}

function renderAML() {
    const container = document.getElementById('amlList');

    const amlData = store.clients.map(client => ({
        client,
        photoIdVerified: client.photoIdVerified || false,
        amlccVerified: client.amlccVerified || false
    }));

    if (amlData.length === 0) {
        container.innerHTML = '<div class="empty-state">No clients for AML checks.</div>';
        return;
    }

    container.innerHTML = amlData.map(({ client, photoIdVerified, amlccVerified }) => `
    <div class="list-item">
      <div class="list-item-info">
        <h4>${client.name}</h4>
        <div class="list-item-meta">
          <span class="status ${photoIdVerified ? 'verified' : 'pending'}">Photo ID: ${photoIdVerified ? '✓' : 'Pending'}</span>
          <span class="status ${amlccVerified ? 'verified' : 'pending'}">AMLCC: ${amlccVerified ? '✓' : 'Pending'}</span>
        </div>
      </div>
      <div class="list-item-actions">
        <button onclick="toggleAML('${client.id}', 'photoId')">📷</button>
        <button onclick="toggleAML('${client.id}', 'amlcc')">🛡️</button>
      </div>
    </div>
  `).join('');
}

function updateStats() {
    document.getElementById('statClients').textContent = store.clients.filter(c => c.status === 'active').length;
    document.getElementById('statTasks').textContent = store.tasks.filter(t => t.status === 'pending').length;

    const pendingPayments = store.payments.filter(p => p.status === 'pending').reduce((sum, p) => sum + p.amount, 0);
    document.getElementById('statPayments').textContent = '£' + pendingPayments.toFixed(0);

    const amlDue = store.clients.filter(c => !c.photoIdVerified || !c.amlccVerified).length;
    document.getElementById('statAML').textContent = amlDue;
}

function populateClientSelects() {
    const options = '<option value="">Select client...</option>' +
        store.clients.map(c => `<option value="${c.id}">${c.name}</option>`).join('');
    document.getElementById('taskClientSelect').innerHTML = options;
    document.getElementById('paymentClientSelect').innerHTML = options;
}

// CRUD Operations
function generateId() {
    return Date.now().toString(36) + Math.random().toString(36).substr(2);
}

// Clients
document.getElementById('addClientBtn').addEventListener('click', () => {
    document.getElementById('clientForm').reset();
    document.getElementById('clientForm').dataset.editId = '';
    document.getElementById('clientModalTitle').textContent = 'Add Client';
    document.getElementById('clientModal').classList.add('active');
});

document.getElementById('clientForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const form = e.target;
    const editId = form.dataset.editId;

    const services = Array.from(form.querySelectorAll('input[name="services"]:checked')).map(cb => cb.value);

    const clientData = {
        name: form.name.value,
        email: form.email.value,
        phone: form.phone.value,
        utr: form.utr.value,
        companyNumber: form.companyNumber.value,
        services,
        status: form.status.value
    };

    if (editId) {
        const index = store.clients.findIndex(c => c.id === editId);
        store.clients[index] = { ...store.clients[index], ...clientData };
    } else {
        store.clients.push({ id: generateId(), ...clientData, photoIdVerified: false, amlccVerified: false });
    }

    saveData();
    document.getElementById('clientModal').classList.remove('active');
    renderAll();
});

window.editClient = function(id) {
    const client = store.clients.find(c => c.id === id);
    const form = document.getElementById('clientForm');
    form.dataset.editId = id;
    document.getElementById('clientModalTitle').textContent = 'Edit Client';

    form.name.value = client.name;
    form.email.value = client.email || '';
    form.phone.value = client.phone || '';
    form.utr.value = client.utr || '';
    form.companyNumber.value = client.companyNumber || '';
    form.status.value = client.status;

    form.querySelectorAll('input[name="services"]').forEach(cb => {
        cb.checked = (client.services || []).includes(cb.value);
    });

    document.getElementById('clientModal').classList.add('active');
};

window.deleteClient = function(id) {
    if (confirm('Delete this client?')) {
        store.clients = store.clients.filter(c => c.id !== id);
        store.tasks = store.tasks.filter(t => t.clientId !== id);
        store.payments = store.payments.filter(p => p.clientId !== id);
        saveData();
        renderAll();
    }
};

// Tasks
document.getElementById('addTaskBtn').addEventListener('click', () => {
    document.getElementById('taskForm').reset();
    populateClientSelects();
    document.getElementById('taskModal').classList.add('active');
});

document.getElementById('taskForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const form = e.target;

    store.tasks.push({
        id: generateId(),
        title: form.title.value,
        clientId: form.clientId.value || null,
        serviceType: form.serviceType.value,
        deadline: form.deadline.value || null,
        description: form.description.value,
        status: 'pending'
    });

    saveData();
    document.getElementById('taskModal').classList.remove('active');
    renderAll();
});

window.toggleTaskStatus = function(id) {
    const task = store.tasks.find(t => t.id === id);
    task.status = task.status === 'completed' ? 'pending' : 'completed';
    saveData();
    renderAll();
};

window.deleteTask = function(id) {
    if (confirm('Delete this task?')) {
        store.tasks = store.tasks.filter(t => t.id !== id);
        saveData();
        renderAll();
    }
};

// Payments
document.getElementById('addPaymentBtn').addEventListener('click', () => {
    document.getElementById('paymentForm').reset();
    populateClientSelects();
    document.getElementById('paymentModal').classList.add('active');
});

document.getElementById('paymentForm').addEventListener('submit', (e) => {
    e.preventDefault();
    const form = e.target;

    store.payments.push({
        id: generateId(),
        clientId: form.clientId.value || null,
        amount: parseFloat(form.amount.value),
        description: form.description.value,
        dueDate: form.dueDate.value || null,
        status: form.status.value
    });

    saveData();
    document.getElementById('paymentModal').classList.remove('active');
    renderAll();
});

window.togglePaymentStatus = function(id) {
    const payment = store.payments.find(p => p.id === id);
    payment.status = payment.status === 'paid' ? 'pending' : 'paid';
    saveData();
    renderAll();
};

window.deletePayment = function(id) {
    if (confirm('Delete this payment?')) {
        store.payments = store.payments.filter(p => p.id !== id);
        saveData();
        renderAll();
    }
};

// AML
window.toggleAML = function(clientId, type) {
    const client = store.clients.find(c => c.id === clientId);
    if (type === 'photoId') client.photoIdVerified = !client.photoIdVerified;
    if (type === 'amlcc') client.amlccVerified = !client.amlccVerified;
    saveData();
    renderAll();
};

// Modal close handlers
document.getElementById('closeClientModal').addEventListener('click', () => {
    document.getElementById('clientModal').classList.remove('active');
});
document.getElementById('cancelClient').addEventListener('click', () => {
    document.getElementById('clientModal').classList.remove('active');
});
document.getElementById('closeTaskModal').addEventListener('click', () => {
    document.getElementById('taskModal').classList.remove('active');
});
document.getElementById('cancelTask').addEventListener('click', () => {
    document.getElementById('taskModal').classList.remove('active');
});
document.getElementById('closePaymentModal').addEventListener('click', () => {
    document.getElementById('paymentModal').classList.remove('active');
});
document.getElementById('cancelPayment').addEventListener('click', () => {
    document.getElementById('paymentModal').classList.remove('active');
});

// Search
document.getElementById('clientSearch').addEventListener('input', renderClients);

// Render all
function renderAll() {
    renderClients();
    renderTasks();
    renderPayments();
    renderAML();
    updateStats();
}

// Initialize
renderAll();
