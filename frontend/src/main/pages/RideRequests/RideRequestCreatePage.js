import BasicLayout from "main/layouts/BasicLayout/BasicLayout";
import RideRequestForm from "main/components/RideRequests/RideRequestForm";
import { Navigate } from 'react-router-dom'
import { useBackendMutation } from "main/utils/useBackend";
import { toast } from "react-toastify";

export default function RideRequestsCreatePage() {

  const objectToAxiosParams = (RideRequest) => ({
    url: "/api/RideRequests/post",
    method: "POST",
    params: {
      day: RideRequest.day,
      fullname: RideRequest.fullName,
      course: RideRequest.course,
      startTime: RideRequest.startTime,
      stopTime: RideRequest.stopTime,
      building: RideRequest.building,
      room: RideRequest.room,
      pickup: RideRequest.pickup
    }
  });

  const onSuccess = (RideRequest) => {
    toast(`New RideRequest Created - id: ${RideRequest.id}`);
  }

  const mutation = useBackendMutation(
    objectToAxiosParams,
     { onSuccess }, 
     // Stryker disable next-line all : hard to set up test for caching
     ["/api/RideRequests/all"]
     );

  const { isSuccess } = mutation

  const onSubmit = async (data) => {
    mutation.mutate(data);
  }

  if (isSuccess) {
    return <Navigate to="/RideRequests/list" />
  }

  return (
    <BasicLayout>
      <div className="pt-2">
        <h1>Create New RideRequest</h1>

        <RideRequestForm submitAction={onSubmit} />

      </div>
    </BasicLayout>
  )
}