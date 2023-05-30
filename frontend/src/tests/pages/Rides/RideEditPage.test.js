import { render, screen, act, waitFor, fireEvent } from "@testing-library/react";
import ParkEditPage from "main/pages/Rides/RideEditPage";
import { QueryClient, QueryClientProvider } from "react-query";
import { MemoryRouter } from "react-router-dom";
import mockConsole from "jest-mock-console";

const mockNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
    ...jest.requireActual('react-router-dom'),
    useParams: () => ({
        id: 3
    }),
    useNavigate: () => mockNavigate
}));

const mockUpdate = jest.fn();
jest.mock('main/utils/rideUtils', () => {
    return {
        __esModule: true,
        rideUtils: {
            update: (ride) => {return mockUpdate();},
            getById: (_id) => {
                return {
                    ride: { // EDIT: CHANGE THIS TEST CASE
                        // look @ park form/table
                        // this is from the park stuff from team01
                        id: 3,
                        name: "Anderson Park",
                        address: "123 Fake Ave",
                        rating: "3.9"
                    }
                }
            }
        }
    }
});


describe("RideEditPage tests", () => {

    const queryClient = new QueryClient();

    test("renders without crashing", () => {
        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ParkEditPage />
                </MemoryRouter>
            </QueryClientProvider>
        );
    });

    test("loads the correct fields", async () => {

        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ParkEditPage />
                </MemoryRouter>
            </QueryClientProvider>
        );

        // TODO: EDIT THIS
        expect(screen.getByTestId("ParkForm-name")).toBeInTheDocument();
        expect(screen.getByDisplayValue('Anderson Park')).toBeInTheDocument();
        expect(screen.getByDisplayValue('123 Fake Ave')).toBeInTheDocument();
        expect(screen.getByDisplayValue('3.9')).toBeInTheDocument();
    });

    test("redirects to /Rides on submit", async () => {

        const restoreConsole = mockConsole();

        // TODO: EDIT THESE VALUES
        mockUpdate.mockReturnValue({
            "park": {
                id: 3,
                name: "Dog Park",
                address: "123 Ave",
                rating: "4"
            }
        });

        render(
            <QueryClientProvider client={queryClient}>
                <MemoryRouter>
                    <ParkEditPage />
                </MemoryRouter>
            </QueryClientProvider>
        )


        // TODO: EDIT THESE VALUES
        const nameInput = screen.getByLabelText("Name");
        expect(nameInput).toBeInTheDocument();


        const addressInput = screen.getByLabelText("Address");
        expect(addressInput).toBeInTheDocument();
        
        const ratingInput = screen.getByLabelText("Rating");
        expect(ratingInput).toBeInTheDocument();

        const updateButton = screen.getByText("Update");
        expect(updateButton).toBeInTheDocument();

        await act(async () => {
            fireEvent.change(nameInput, { target: { value: 'Dog Park' } })
            fireEvent.change(addressInput, { target: { value: '123 Ave' } })
            fireEvent.change(ratingInput, { target: { value: '4' } })
            fireEvent.click(updateButton);
        });

        await waitFor(() => expect(mockUpdate).toHaveBeenCalled());
        await waitFor(() => expect(mockNavigate).toHaveBeenCalledWith("/Rides"));

        // assert - check that the console.log was called with the expected message
        expect(console.log).toHaveBeenCalled();
        const message = console.log.mock.calls[0][0];
        const expectedMessage =  `updatedPark: {"park":{"id":3,"name":"Dog Park","address":"123 Ave","rating":"4"}`

        expect(message).toMatch(expectedMessage);
        restoreConsole();

    });

});