import '../Styles/adminnavbar.css'
import { useNavigate } from 'react-router-dom';
import Dropdown from 'react-bootstrap/Dropdown';

const Usernavbar = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('userId');
        navigate('/userLogin');
    };

    return (
        <div className="adminnavbar">
            <div className="logo" style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                width: "100%"
            }}>
                <h1><span>~Be</span>fik<span> / </span>Re<span>Yatra</span></h1>

                <Dropdown className="user-profile-dropdown" align="end">
                    <Dropdown.Toggle
                        variant="light"
                        id="dropdown-profile"
                        style={{
                            background: 'white',
                            border: '1px solid #ddd',
                            borderRadius: '30px',
                            display: 'flex',
                            alignItems: 'center',
                            gap: '10px',
                            padding: '5px 15px',
                            boxShadow: '0 2px 4px rgba(0,0,0,0.05)'
                        }}
                    >
                        <div style={{
                            width: '32px',
                            height: '32px',
                            borderRadius: '50%',
                            background: '#1a1f36',
                            color: 'white',
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            fontWeight: 'bold'
                        }}>
                            J
                        </div>
                        <div style={{ textAlign: 'left', lineHeight: '1.2' }}>
                            <div style={{ fontWeight: 'bold', color: '#1a1a1a' }}>John Doe</div>
                            <div style={{ fontSize: '12px', color: '#666' }}>My Profile</div>
                        </div>
                    </Dropdown.Toggle>

                    <Dropdown.Menu style={{ width: '300px', marginTop: '10px', padding: '10px 0', border: 'none', boxShadow: '0 4px 12px rgba(0,0,0,0.1)', borderRadius: '12px' }}>
                        <Dropdown.Item onClick={() => navigate('/userbookings')} style={{ padding: '10px 20px' }}>
                            <div style={{ fontWeight: 'bold', color: '#1a1a1a', fontSize: '15px' }}>My Bookings</div>
                            <div style={{ fontSize: '13px', color: '#666' }}>View & manage bookings</div>
                        </Dropdown.Item>

                        <Dropdown.Item href="#/travellers" style={{ padding: '10px 20px' }}>
                            <div style={{ fontWeight: 'bold', color: '#666', fontSize: '15px' }}>My Travellers</div>
                            <div style={{ fontSize: '13px', color: '#999' }}>Saved passengers (coming soon)</div>
                        </Dropdown.Item>

                        <Dropdown.Divider />

                        <Dropdown.Item onClick={handleLogout} style={{ padding: '10px 20px', fontWeight: 'bold', color: '#1a1a1a' }}>
                            Logout
                        </Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>
            </div>
        </div>
    );
}

export default Usernavbar;