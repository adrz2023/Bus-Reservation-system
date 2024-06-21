import Dropdown from 'react-bootstrap/Dropdown';
import '../Styles/dropdown.css';
const NavDropdown = () => {

    return (
        <Dropdown>
          <Dropdown.Toggle variant="success" id="dropdown-basic">
            Account
          </Dropdown.Toggle>
    
          <Dropdown.Menu>
            <Dropdown.Item href="/adminhomepage/addbus">AddBus</Dropdown.Item>
            <Dropdown.Item href="#/action-2">Busess List</Dropdown.Item>
            <Dropdown.Item href="#/action-3">Edit Profile</Dropdown.Item>
            <Dropdown.Item href="#/action-4">Logout</Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
      );
}
 
export default NavDropdown;