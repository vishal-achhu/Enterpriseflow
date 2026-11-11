import { useEffect, useState } from 'react';
import { Activity, ArrowUpRight, Boxes, CircleDollarSign, ClipboardList, Users, RefreshCw, LogIn } from 'lucide-react';
import { api, token } from './api';

type Summary = {customerCount:number;orderCount:number;openOrders:number;revenue:number};
type Customer = {id:number;name:string;email:string;company?:string};
type Order = {id:number;reference:string;status:string;total:number;customer:string};

export default function App() {
  const [loggedIn,setLoggedIn] = useState(Boolean(token()));
  const [email,setEmail] = useState('demo@example.com');
  const [password,setPassword] = useState('');
  const [summary,setSummary] = useState<Summary | null>(null);
  const [customers,setCustomers] = useState<Customer[]>([]);
  const [orders,setOrders] = useState<Order[]>([]);
  const [message,setMessage] = useState('');
  const [forecast,setForecast] = useState<number[]|null>(null);

  async function load() {
    try {
      const [s,c,o] = await Promise.all([
        api<Summary>('/dashboard/summary'), api<Customer[]>('/customers'), api<Order[]>('/orders')
      ]);
      setSummary(s); setCustomers(c); setOrders(o); setMessage('');
    } catch(e) { setMessage(e instanceof Error ? e.message : 'Unable to load dashboard'); }
  }
  useEffect(()=>{ if(loggedIn) void load(); },[loggedIn]);

  async function login(e:React.FormEvent) {
    e.preventDefault();
    try {
      const result = await api<{accessToken:string}>('/auth/login',{method:'POST',body:JSON.stringify({email,password})});
      localStorage.setItem('ef_token',result.accessToken); setLoggedIn(true); setMessage('');
    } catch(err) { setMessage(err instanceof Error ? err.message : 'Login failed'); }
  }
  async function runForecast() {
    try {
      const result = await api<{forecast:number[];method:string}>('/analytics/forecast',{
        method:'POST',body:JSON.stringify({daily_orders:[8,10,9,13,12,15,14],horizon_days:7})
      });
      setForecast(result.forecast); setMessage(`Forecast method: ${result.method}`);
    } catch(e) { setMessage(e instanceof Error ? e.message : 'Forecast failed'); }
  }
  if(!loggedIn) return <main className="login-wrap"><form className="login-card" onSubmit={login}>
    <div className="brand-mark"><Boxes size={24}/></div><p className="eyebrow">ENTERPRISE OPERATIONS</p>
    <h1>Welcome to EnterpriseFlow</h1><p className="muted">Sign in to your operations workspace.</p>
    <label>Email<input type="email" value={email} onChange={e=>setEmail(e.target.value)} required/></label>
    <label>Password<input type="password" value={password} onChange={e=>setPassword(e.target.value)} required/></label>
    <button className="primary"><LogIn size={16}/> Sign in</button>
    <p className="hint">Create an account first with POST /api/auth/register.</p>{message && <p className="error">{message}</p>}
  </form></main>;

  return <div className="shell">
    <aside className="sidebar"><div className="brand"><span className="brand-mark"><Boxes size={21}/></span><b>EnterpriseFlow</b></div>
      <div className="workspace-label">WORKSPACE</div><div className="nav active"><Activity size={17}/> Overview</div>
      <div className="nav"><Users size={17}/> Customers</div><div className="nav"><ClipboardList size={17}/> Orders</div>
      <div className="sidebar-bottom"><div className="avatar">VB</div><div><b>Operations</b><small>Workspace admin</small></div><button className="logout" onClick={()=>{localStorage.removeItem('ef_token');setLoggedIn(false)}}>Exit</button></div>
    </aside>
    <main className="content">
      <header><div><p className="eyebrow">MONITORING / OVERVIEW</p><h1>Operations overview</h1><p className="muted">A live view of your customer and order activity.</p></div>
        <button className="secondary" onClick={()=>void load()}><RefreshCw size={15}/> Refresh data</button></header>
      {message && <div className="notice">{message}</div>}
      <section className="metrics">
        <Metric icon={<Users/>} label="Total customers" value={summary?.customerCount ?? '—'} tone="blue"/>
        <Metric icon={<ClipboardList/>} label="Total orders" value={summary?.orderCount ?? '—'} tone="violet"/>
        <Metric icon={<Activity/>} label="Open orders" value={summary?.openOrders ?? '—'} tone="amber"/>
        <Metric icon={<CircleDollarSign/>} label="Tracked revenue" value={summary ? `₹${Number(summary.revenue).toLocaleString('en-IN')}` : '—'} tone="green"/>
      </section>
      <section className="panel-grid">
        <div className="panel"><div className="panel-head"><div><p className="eyebrow">CUSTOMER RELATIONSHIPS</p><h2>Recent customers</h2></div><span className="count">{customers.length} records</span></div>
          <div className="table-wrap"><table><thead><tr><th>Customer</th><th>Company</th><th>Email</th></tr></thead><tbody>
            {customers.slice(0,6).map(c=><tr key={c.id}><td><b>{c.name}</b></td><td>{c.company || '—'}</td><td>{c.email}</td></tr>)}
            {!customers.length && <tr><td colSpan={3} className="empty">No customers yet. Add one through POST /api/customers.</td></tr>}
          </tbody></table></div>
        </div>
        <div className="panel"><div className="panel-head"><div><p className="eyebrow">INTELLIGENCE SERVICE</p><h2>Demand forecast</h2></div><span className="status">PYTHON API</span></div>
          <p className="muted">Generate a 7-day baseline projection using recent order counts.</p>
          <button className="primary" onClick={()=>void runForecast()}>Run forecast <ArrowUpRight size={16}/></button>
          {forecast && <div className="forecast">{forecast.map((v,i)=><div key={i}><span>Day {i+1}</span><b>{v}</b></div>)}</div>}
        </div>
      </section>
      <section className="panel orders-panel"><div className="panel-head"><div><p className="eyebrow">FULFILMENT</p><h2>Latest orders</h2></div><span className="count">{orders.length} records</span></div>
        <div className="table-wrap"><table><thead><tr><th>Reference</th><th>Customer</th><th>Status</th><th>Total</th></tr></thead><tbody>
          {orders.slice(0,8).map(o=><tr key={o.id}><td><b>{o.reference}</b></td><td>{o.customer}</td><td><span className={`pill ${o.status.toLowerCase()}`}>{o.status}</span></td><td>₹{Number(o.total).toLocaleString('en-IN')}</td></tr>)}
          {!orders.length && <tr><td colSpan={4} className="empty">No orders yet. Create an order through POST /api/orders.</td></tr>}
        </tbody></table></div>
      </section>
      <footer>EnterpriseFlow · Java business services · Python analytics · TypeScript interface</footer>
    </main>
  </div>;
}
function Metric({icon,label,value,tone}:{icon:React.ReactNode;label:string;value:string|number;tone:string}) {
 return <article className="metric"><div className={`metric-icon ${tone}`}>{icon}</div><p>{label}</p><strong>{value}</strong><small>Current workspace</small></article>;
}
