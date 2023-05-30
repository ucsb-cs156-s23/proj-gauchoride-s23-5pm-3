import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { useParams } from "react-router-dom";
import { rideUtils }  from 'main/utils/rideUtils';
import RideForm from 'main/components/Rides/RideForm';
import { useNavigate } from 'react-router-dom'


export default function RideEditPage() {
    let { id } = useParams();

    let navigate = useNavigate(); 

    const response = rideUtils.getById(id);

    const onSubmit = async (ride) => {
        const updatedRide = rideUtils.update(ride);
        console.log("updatedRide: " + JSON.stringify(updatedRide));
        navigate("/rides");
    }  

    return (
        <BasicLayout>
            <div className="pt-2">
                <h1>Edit Park</h1>
                <RideForm submitAction={onSubmit} buttonLabel={"Update"} initialContents={response.ride}/>
            </div>
        </BasicLayout>
    )
}