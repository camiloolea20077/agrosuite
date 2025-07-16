import { CreateEmployeesDto } from "./create-employees.dto";

export interface UpdateEmployeesDto extends CreateEmployeesDto {
    id: number
}