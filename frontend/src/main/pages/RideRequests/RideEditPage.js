// WIP c/p from team03, need to edit/change

import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import { useParams } from "react-router-dom";
import RideForm from "main/components/RideRequests/RideForm";
import { Navigate } from 'react-router-dom'
import { useBackend, useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";

export default function RidesEditPage() {
  let { id } = useParams();

  const { data: Ride, error, status } =
    useBackend(
      // Stryker disable next-line all : don't test internal caching of React Query
      [`/api/RideRequests?id=${id}`],
      {  // Stryker disable next-line all : GET is the default, so changing this to "" doesn't introduce a bug
        method: "GET",
        url: `/api/RidesRequests`,
        params: {
          id
        }
      }
    );


  const objectToAxiosPutParams = (Ride) => ({
    url: "/api/RidesRequests",
    method: "PUT",
    params: {
      id: Ride.id,
    },


    data: {
      givenName: Ride.givenName,
      familyName: Ride.familyName,
      email: Ride.email,
      admin: Ride.admin,
      driver: Ride.driver
    }
  });

  const onSuccess = (Ride) => {
    toast(`Ride Updated - id: ${Ride.id} givenName: ${Ride.givenName} email: ${Ride.email} admin: ${Ride.admin} driver: ${Ride.driver}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosPutParams,
    { onSuccess },
    // Stryker disable next-line all : hard to set up test for caching
    [`/api/RideRequests?id=${id}`]
  );

  const { isSuccess } = mutation

  const onSubmit = async (data) => {
    mutation.mutate(data);
  }

  if (isSuccess) {
    return <Navigate to="/RidesRequests/list" />
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Edit Ride</h1>
        {Ride &&
          <RideForm initialContents={Ride} submitAction={onSubmit} buttonLabel="Update" />
        }
      </div>
    </BasicLayout>
  )
}


/*

// accidentally used team01, but going to change to team03 stuff
// keeping the below as reference

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

*